package com.electroStore.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.Product;

@Repository
public interface ProductRepo  extends JpaRepository<Product, String>{

	
	Page<Product> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
