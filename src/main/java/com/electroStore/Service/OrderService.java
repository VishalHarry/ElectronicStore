package com.electroStore.Service;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.electroStore.DTOs.CreateOrderRequest;
import com.electroStore.DTOs.OrderDto;
import com.electroStore.DTOs.PagableResponse;

public interface OrderService {
	
	public OrderDto createdOrder(CreateOrderRequest request) throws BadRequestException;
	
	public void removeOrder(String orderId);
	
	PagableResponse<OrderDto> getAllOrdersOfUser(String userId,int pageNum,int  pageSize,String sortBy,String sortOrder);
	

}
