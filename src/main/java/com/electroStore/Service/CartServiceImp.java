package com.electroStore.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electroStore.DTOs.AddItemToCartReq;
import com.electroStore.DTOs.CartDto;
import com.electroStore.DTOs.CartItemDto;
import com.electroStore.Entities.Cart;
import com.electroStore.Entities.Product;
import com.electroStore.Entities.User;
import com.electroStore.Entities.cartItem;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Repositories.CartItemRepo;
import com.electroStore.Repositories.CartRepo;
import com.electroStore.Repositories.ProductRepo;
import com.electroStore.Repositories.UserRepo;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartItemRepo cartItemRepo;

    @Override
    public CartDto addItemToCart(String userID, AddItemToCartReq req) {

        String productId = req.getId();
        int quantity = req.getQuantity();

        // check product
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with this id: " + productId));

        // check user
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundExceptions("User not found with this id: " + userID));

        // check cart
        Cart cart;
        try {
            cart = cartRepo.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString()); 
            cart.setCreatedDate(LocalDate.now()); // only if createdDate is LocalDate
        }

        boolean updated = false;

        // update if product already in cart
        for (cartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated = true;
                break;
            }
        }

        // if product not already present, add new
        if (!updated) {
            cartItem cartItems = cartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItems);
        }

        cart.setUser(user);

        Cart saved = cartRepo.save(cart);

        return modelMapper.map(saved, CartDto.class);
    }

    @Override
    public void removeCartItem(String userId, int cartItemId) {
        cartItem items=cartItemRepo.findById(cartItemId)
        		.orElseThrow(() -> new ResourceNotFoundExceptions("CartItem not found with this id: " + cartItemId));
         cartItemRepo.delete(items);
         
    }

    @Override
    public void clearCart(String userID) {
    	User user = userRepo.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundExceptions("User not found with this id: " + userID)); 
    	
    	Cart cart =cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundExceptions("Cart not found with this id: "));
    	cart.getItems().clear();
    	cartRepo.save(cart);
    	
    }

	@Override
	public CartDto getCartByUser(String id) {
		User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions("User not found with this id: " + id)); 
    	
    	Cart cart =cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundExceptions("Cart not found with this id: "));
		return modelMapper.map(cart, CartDto.class);
	}
}
