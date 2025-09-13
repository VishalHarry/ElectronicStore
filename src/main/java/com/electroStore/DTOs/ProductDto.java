package com.electroStore.DTOs;

import java.util.Date;

import com.electroStore.Entities.Category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String id;

    @NotBlank(message = "Product title cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Integer price;

    @Min(value = 0, message = "Discount price cannot be negative")
    private Integer dicountPric;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    private Date addedDate;

    @NotNull(message = "Live status must be specified (true/false)")
    private Boolean live;

    @NotNull(message = "Stock status must be specified (true/false)")
    private Boolean stock;
    
    
    private String image;
    
    private CategoryDto category;

    
}
