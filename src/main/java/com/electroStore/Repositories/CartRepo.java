package com.electroStore.Repositories;



import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.Cart;
import com.electroStore.Entities.User;

@Repository
public interface CartRepo extends JpaRepository<Cart, String>{
	
	Optional<Cart> findByUser(User user);

}
