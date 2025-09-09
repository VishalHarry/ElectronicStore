package com.electroStore.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.electroStore.Entities.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category,String> {

}
