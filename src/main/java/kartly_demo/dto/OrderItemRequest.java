package kartly_demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;
}
