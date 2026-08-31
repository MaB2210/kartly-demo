package kartly_demo.integration;

import kartly_demo.dto.CreateOrderRequest;
import kartly_demo.dto.OrderItemRequest;
import kartly_demo.entity.OrderEntity;
import kartly_demo.entity.ProductEntity;
import kartly_demo.entity.UserEntity;
import kartly_demo.repository.ProductRepository;
import kartly_demo.repository.UserRepository;
import kartly_demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class OrderServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("kartly_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configurePropertis(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createOrder_persistsToRealDatabase(){
        UserEntity user = new UserEntity();
        user.setEmail("integration@test.com");
        user.setName("Integration Test User");
        user.setPasswordHash("fake-hash-for-test");
        user = userRepository.save(user);

        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(19.99));
        product.setCategory("Test");
        product = productRepository.save(product);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(2);

        CreateOrderRequest request =new CreateOrderRequest();
        request.setUserId(user.getId());
        request.setItems(List.of(itemRequest));

        OrderEntity saveOrder = orderService.createOrder(request);

        assertThat(saveOrder.getId()).isNotNull();
        assertThat(saveOrder.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(39.98));

        OrderEntity retrieved = orderService.getOrder(saveOrder.getId());
        assertThat(retrieved.getUser().getEmail()).isEqualTo("integration@test.com");
    }
}
