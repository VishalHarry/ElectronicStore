package com.electroStore.DTOs;

import lombok.Data;

@Data
public class AddItemToCartReq {
	
	private String id;
	
	private int quantity;

}
