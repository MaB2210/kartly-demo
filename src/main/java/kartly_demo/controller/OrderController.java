package kartly_demo.controller;

import kartly_demo.dto.CreateOrderRequest;
import kartly_demo.dto.OrderItemRequest;
import kartly_demo.entity.OrderEntity;
import kartly_demo.entity.OrderItemEntity;
import kartly_demo.entity.ProductEntity;
import kartly_demo.repository.OrderRepository;
import kartly_demo.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this. productRepository = productRepository;
    }

    @GetMapping
    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public OrderEntity getOrder(@PathVariable Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: "+id));
    }

    @PostMapping
    public OrderEntity createOrder(@RequestBody CreateOrderRequest request){
        OrderEntity order = new OrderEntity();
        order.setCustomerName(request.getCustomerName());

        BigDecimal total = BigDecimal.ZERO;
        for(OrderItemRequest itemRequest: request.getItems()){
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found : "+itemRequest.getProductId()));

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
