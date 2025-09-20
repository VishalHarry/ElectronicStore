package com.electroStore.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.Order;
import com.electroStore.Entities.User;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {
    
    // Correct method signature
    Page<Order> findByUser(User user, Pageable pageable);
}
