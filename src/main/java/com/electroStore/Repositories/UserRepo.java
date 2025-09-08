package com.electroStore.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

	User findByEmailIgnoreCase(String email);
	
	List<User> findByNameContaining(String keyword);


}
