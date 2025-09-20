package com.electroStore.DTOs;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "UserId is required")
    private String userId;

    @NotBlank(message = "CartId is required")
    private String cartId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
}
