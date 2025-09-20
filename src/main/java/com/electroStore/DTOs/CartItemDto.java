package com.electroStore.DTOs;

import java.sql.Date;
import java.util.List;

import com.electroStore.Entities.Cart;
import com.electroStore.Entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
	
	private int id;
	private ProductDto product;
	private int quantity;
	private int totalPrice;
	
}
