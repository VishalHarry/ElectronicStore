package com.electroStore.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.electroStore.DTOs.CreateOrderRequest;
import com.electroStore.DTOs.OrderDto;
import com.electroStore.DTOs.PagableResponse;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Helper.Messageresponce;
import com.electroStore.Service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1️⃣ Create Order
    @PostMapping("/")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            OrderDto orderDto = orderService.createdOrder(request);
            return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 2️⃣ Remove Order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Messageresponce> deleteOrder(@PathVariable String orderId) {
        Messageresponce resp = new Messageresponce();
        try {
            orderService.removeOrder(orderId);
            resp.setMessage("Order deleted successfully!");
            resp.setStatus(HttpStatus.OK);
            resp.setSuccess(true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (ResourceNotFoundExceptions e) {
            resp.setMessage(e.getMessage());
            resp.setStatus(HttpStatus.NOT_FOUND);
            resp.setSuccess(false);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
        }
    }


    // 3️⃣ Get All Orders of User with Pagination & Sorting
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagableResponse<OrderDto>> getOrdersOfUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        PagableResponse<OrderDto> response = orderService.getAllOrdersOfUser(userId, pageNum, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
