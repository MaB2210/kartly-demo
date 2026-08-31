package kartly_demo.service;

import kartly_demo.dto.CreateOrderRequest;
import kartly_demo.dto.OrderItemRequest;
import kartly_demo.entity.OrderEntity;
import kartly_demo.entity.ProductEntity;
import kartly_demo.entity.UserEntity;
import kartly_demo.exception.ResourceNotFoundException;
import kartly_demo.repository.OrderRepository;
import kartly_demo.repository.ProductRepository;
import kartly_demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService oRderService;

    @Test
    void createOrder_calculatesTotalCorrectly(){
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Test User");

        ProductEntity product = new ProductEntity();
        product.setId(5L);
        product.setName("Wireless Mouse");
        product.setPrice(BigDecimal.valueOf(29.99));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(orderRepository.save(org.mockito.ArgumentMatchers.any(OrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(5L);
        itemRequest.setQuantity(3);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setItems(List.of(itemRequest));

        OrderEntity result = oRderService.createOrder(request);

        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(89.97));
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    void createOrder_throwsWhenUserNotFound(){
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(999L);
        request.setItems(List.of());

        assertThrows(ResourceNotFoundException.class, () -> oRderService.createOrder(request));
    }
}
