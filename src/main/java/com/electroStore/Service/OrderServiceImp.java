package com.electroStore.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.electroStore.DTOs.CreateOrderRequest;
import com.electroStore.DTOs.OrderDto;
import com.electroStore.DTOs.PagableResponse;
import com.electroStore.Entities.Cart;
import com.electroStore.Entities.Order;
import com.electroStore.Entities.OrderItem;
import com.electroStore.Entities.User;
import com.electroStore.Entities.cartItem;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Repositories.CartRepo;
import com.electroStore.Repositories.OrderRepo;
import com.electroStore.Repositories.UserRepo;

@Service
public class OrderServiceImp implements OrderService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CartRepo cartRepo;

	@Override
	public OrderDto createdOrder(CreateOrderRequest request) throws BadRequestException {

	    // 1. Get user
	    User user = userRepo.findById(request.getUserId())
	            .orElseThrow(() -> new ResourceNotFoundExceptions("User not found with id: " + request.getUserId()));

	    // 2. Get cart
	    Cart cart = cartRepo.findById(request.getCartId())
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Cart not found with id: " + request.getCartId()));

	    // 3. Validate cart items
	    List<cartItem> items = cart.getItems();
	    if (items.isEmpty()) {
	        throw new BadRequestException("Cart is empty, cannot place order!");
	    }

	    // 4. Convert CartItems -> OrderItems
	    List<OrderItem> orderItems = new ArrayList<>();
	    double totalAmount = 0.0;

	    for (cartItem cartItem : items) {
	        OrderItem orderItem = OrderItem.builder()
	                .order(null) // will set later
	                .product(cartItem.getProduct())
	                .quantity(cartItem.getQuantity())
	                .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice())
	                .build();

	        totalAmount += orderItem.getTotalPrice();
	        orderItems.add(orderItem);
	    }

	    // 5. Build Order
	    Order order = Order.builder()
	            .user(user)
	            .items(orderItems)
	            .orderDate(LocalDateTime.now())
	            .status("PENDING") // default
	            .totalAmount(totalAmount)
	            .paymentMethod(request.getPaymentMethod())
	            .shippingAddress(request.getShippingAddress())
	            .build();

	    // Set order reference in each orderItem
	    orderItems.forEach(item -> item.setOrder(order));

	    // 6. Save Order
	    Order savedOrder = orderRepo.save(order);

	    // 7. Clear the cart
	    cart.getItems().clear();
	    cartRepo.save(cart);

	    // 8. Convert to DTO and return
	    return modelMapper.map(savedOrder, OrderDto.class);
	}


	@Override
	public void removeOrder(String orderId) {
	  Order order=orderRepo.findById(orderId)
			  .orElseThrow(() -> new ResourceNotFoundExceptions("Order not found with id: " + orderId));
	  
	   orderRepo.delete(order);
		
	}

	@Override
	public PagableResponse<OrderDto> getAllOrdersOfUser(String userId, int pageNum, int pageSize, String sortBy, String sortOrder) {

	    // 1. Get User
	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("User not found with id: " + userId));

	    // 2. Sort Direction
	    Sort sort = sortOrder.equalsIgnoreCase("desc") ?
	            Sort.by(sortBy).descending() :
	            Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

	    // 3. Fetch Orders
	    Page<Order> page = orderRepo.findByUser(user, pageable);

	    // 4. Convert Entity -> DTO
	    List<OrderDto> dtos = page.getContent().stream()
	            .map(order -> modelMapper.map(order, OrderDto.class))
	            .toList();

	    // 5. Wrap in PagableResponse
	    PagableResponse<OrderDto> response = new PagableResponse<>();
	    response.setContent(dtos);
	    response.setPageNum(page.getNumber());
	    response.setPageSize(page.getSize());
	    response.setTotalElement(page.getTotalElements());
	    response.setTotalPage(page.getTotalPages());
	    response.setLastpage(page.isLast());

	    return response;
	}


}
