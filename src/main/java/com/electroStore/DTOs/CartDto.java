package com.electroStore.DTOs;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
	
	private String cartId;
	private LocalDate createdDate;
	private UserDto user;
	private List<CartItemDto> items=new ArrayList<>();

}
