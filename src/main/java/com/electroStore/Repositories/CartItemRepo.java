package com.electroStore.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.cartItem;

@Repository
public interface CartItemRepo extends JpaRepository<cartItem, Integer> {

}
