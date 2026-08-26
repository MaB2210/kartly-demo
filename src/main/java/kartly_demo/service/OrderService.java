package kartly_demo.service;

import kartly_demo.dto.CreateOrderRequest;
import kartly_demo.dto.OrderItemRequest;
import kartly_demo.entity.OrderEntity;
import kartly_demo.entity.OrderItemEntity;
import kartly_demo.entity.ProductEntity;
import kartly_demo.entity.UserEntity;
import kartly_demo.exception.ResourceNotFoundException;
import kartly_demo.repository.OrderRepository;
import kartly_demo.repository.ProductRepository;
import kartly_demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    public OrderEntity getOrder(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: "+id));
    }

    public List<OrderEntity> getOrdersForUser(Long userId){
        return orderRepository.findByUserId(userId);
    }

    public OrderEntity createOrder(CreateOrderRequest request){
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        OrderEntity order = new OrderEntity();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for(OrderItemRequest itemRequest: request.getItems()){
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));
            OrderItemEntity item = new OrderItemEntity();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setOrder(order);
            order.getItems().add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf((itemRequest.getQuantity()))));
        }
        order.setTotalAmount((total));
        return orderRepository.save(order);
     }
}
