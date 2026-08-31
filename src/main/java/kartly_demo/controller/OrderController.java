package kartly_demo.controller;

import jakarta.validation.Valid;
import kartly_demo.dto.CreateOrderRequest;
import kartly_demo.entity.OrderEntity;
import kartly_demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderEntity> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderEntity getOrder(@PathVariable Long id){
        return orderService.getOrder(id);
    }

    @GetMapping("/user/{userId}")
    public List<OrderEntity> getOrdersForUser(@PathVariable Long userId){
        return orderService.getOrdersForUser(userId);
    }

    @PostMapping
    public OrderEntity createOrder(@Valid @RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }
}
