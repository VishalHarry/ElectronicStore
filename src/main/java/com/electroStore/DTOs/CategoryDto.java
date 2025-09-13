package com.electroStore.DTOs;

import java.util.ArrayList;
import java.util.List;

import com.electroStore.Entities.Product;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
	
	    private String categoryId;

	    @NotBlank(message = "Title is mandatory")
	    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
	    private String title;

	    @NotBlank(message = "Description cannot be empty")
	    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
	    private String description;

	    @NotBlank(message = "Cover image URL cannot be empty")
	    private String coverImage;
	    
	   

}
