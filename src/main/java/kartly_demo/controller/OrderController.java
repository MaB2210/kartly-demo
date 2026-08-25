package kartly_demo.controller;

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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository,UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public OrderEntity getOrder(@PathVariable Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: "+id));
    }

    @GetMapping("/user/{userId}")
    public List<OrderEntity> getOrdersForUser(@PathVariable Long userId){
        return orderRepository.findByUserId(userId);
    }

    @PostMapping
    public OrderEntity createOrder(@RequestBody CreateOrderRequest request){
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: "+ request.getUserId()));

        OrderEntity order = new OrderEntity();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for(OrderItemRequest itemRequest: request.getItems()){
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found : "+itemRequest.getProductId()));

            OrderItemEntity item = new OrderItemEntity();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setOrder((order));

            order.getItems().add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
}
