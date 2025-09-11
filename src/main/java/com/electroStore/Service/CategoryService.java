package com.electroStore.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.CategoryDto;
import com.electroStore.DTOs.PagableResponse;

public interface CategoryService {
	
	public CategoryDto create(CategoryDto dto) throws IOException;
	
	public CategoryDto updateCategory(CategoryDto dto,String id ) throws IOException;
	
	public void deleteCategory(String id) throws Exception;
	
	public PagableResponse<CategoryDto> getAllCategory(int pagaNumber,int pageSize,String sortBy,String sortOrder);
	
	public CategoryDto getSingleCategory(String id);
	
	public CategoryDto uploadImage(String id,MultipartFile file) throws IOException;
	
	public CategoryDto updateImage(String id,MultipartFile file) throws IOException, Exception;
	
	public CategoryDto deleteImage(String id) throws IOException, Exception;
	
	

}
