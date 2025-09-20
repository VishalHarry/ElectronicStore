package com.electroStore.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    
    private Long id;

   
    private String orderId;

    @NotNull(message = "Product ID is required")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Positive(message = "Total price must be positive")
    private double totalPrice;
}
