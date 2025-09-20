package com.electroStore.Service;

import com.electroStore.DTOs.AddItemToCartReq;
import com.electroStore.DTOs.CartDto;

public interface CartService {
	
	CartDto addItemToCart(String userID,AddItemToCartReq req) ;
	void removeCartItem(String userId,int cartItemId);
	void clearCart(String userID);
	
	CartDto getCartByUser(String id);
	
	

}
