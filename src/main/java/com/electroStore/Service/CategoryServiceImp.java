package com.electroStore.Service;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.CategoryDto;
import com.electroStore.DTOs.PagableResponse;
import com.electroStore.Entities.Category;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Helper.HelperFun;
import com.electroStore.Repositories.CategoryRepo;

@Service
public class CategoryServiceImp  implements CategoryService{
	@Autowired
	private CategoryRepo repo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ImageUploadService imageUploadService;

	@Override
	public CategoryDto create(CategoryDto dto) throws IOException {
		String categoeryID=UUID.randomUUID().toString();
		dto.setCategoryId(categoeryID);

	   
		Category category=modelMapper.map(dto, Category.class);
		
		Category saved=repo.save(category);
		return modelMapper.map(saved, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto dto, String id) throws IOException {
		Category savedCategory=repo.findById(id).orElseThrow(()-> new ResourceNotFoundExceptions("Category not found with id: " + id));
		savedCategory.setTitle(dto.getTitle());
		savedCategory.setDescription(dto.getDescription());
		
		Category updated=repo.save(savedCategory);
		return modelMapper.map(updated, CategoryDto.class);
	}

	@Override
	public void deleteCategory(String id) throws Exception {
		Category savedCategory=repo.findById(id).orElseThrow(()-> new ResourceNotFoundExceptions("Category not found with id: " + id));
		
		repo.delete(savedCategory);
	}

	@Override
	public PagableResponse<CategoryDto> getAllCategory(int pagaNumber,int pageSize,String sortBy,String sortOrder) {
		
		Sort sort=(sortOrder.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pagable=PageRequest.of(pagaNumber, pageSize, sort);
		Page<Category> category=repo.findAll(pagable);
		PagableResponse<CategoryDto> resp=HelperFun.getPageResponse(category, CategoryDto.class, modelMapper);
		
		return resp;
	}

	@Override
	public CategoryDto getSingleCategory(String id) {
		Category savedCategory=repo.findById(id).orElseThrow(()-> new ResourceNotFoundExceptions("Category not found with id: " + id));
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

}
