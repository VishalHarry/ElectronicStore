package com.electroStore.DTOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
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
public class OrderDto {

    private String id;

    @NotNull(message = "User is required")
    private UserDto user;   // Use UserDto instead of entity

    @NotNull(message = "Order items cannot be empty")
    private List<OrderItemDto> items = new ArrayList<>();

    private LocalDateTime orderDate;

    @NotBlank(message = "Status is required")
    private String status;   // e.g. PENDING, SHIPPED, DELIVERED

    @Positive(message = "Total amount must be positive")
    private Double totalAmount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;  // e.g. COD, UPI, CARD

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
}
