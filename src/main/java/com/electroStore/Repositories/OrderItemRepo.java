package com.electroStore.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}
