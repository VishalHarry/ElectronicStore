package com.electroStore.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.electroStore.DTOs.AddItemToCartReq;
import com.electroStore.DTOs.CartDto;
import com.electroStore.Service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable String userId,
            @RequestBody AddItemToCartReq req) {

        CartDto cart = cartService.addItemToCart(userId, req);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

 
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable String userId,
            @PathVariable int cartItemId) {

        cartService.removeCartItem(userId, cartItemId);
        return new ResponseEntity<>("Item removed from cart successfully", HttpStatus.OK);
    }


    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
    }

  
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cart = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
