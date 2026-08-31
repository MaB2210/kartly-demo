package kartly_demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kartly_demo.dto.CreateOrderRequest;
import kartly_demo.entity.OrderEntity;
import kartly_demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order placement and order history")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders (admin/debug use)")
    @GetMapping
    public List<OrderEntity> getAllOrders(){
        return orderService.getAllOrders();
    }

    @Operation(summary = "Get a single order by ID")
    @GetMapping("/{id}")
    public OrderEntity getOrder(@PathVariable Long id){
        return orderService.getOrder(id);
    }

    @Operation(summary = "Get all orders for a specific user")
    @GetMapping("/user/{userId}")
    public List<OrderEntity> getOrdersForUser(@PathVariable Long userId){
        return orderService.getOrdersForUser(userId);
    }

    @Operation(summary = "Place a new order", description = "Validates the user and each product exist, calculates the total server-side, and persists the order with its line items.")
    @PostMapping
    public OrderEntity createOrder(@Valid @RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);
    }
}
