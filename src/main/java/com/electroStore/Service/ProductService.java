package com.electroStore.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.CategoryDto;
import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.ProductDto;
import com.electroStore.Helper.Messageresponce;

public interface ProductService {
	
	
	public ProductDto createProduct(ProductDto dto);
	
	
	public ProductDto updateProduct(ProductDto dto,String id);
	
	public void deleteproduct(String id) throws Exception;
	
	public PagableResponse<ProductDto> getAllProduct( int pageNumber, int pageSize, String sortBy, String sortOrder);
	
	public ProductDto getSingleProduct(String id);
	
	public PagableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);
	
    public ProductDto uploadImage(String id,MultipartFile file) throws IOException;
	
	public ProductDto updateImage(String id,MultipartFile file) throws IOException, Exception;
	
	public ProductDto deleteImage(String id) throws IOException, Exception;
	
	public ProductDto createProductWithCategory(ProductDto dto,String categoryId);
	
	public ProductDto updateCategoryOFProductDto(String productId,String CategoryId);
	 
	
	
	
	
	
	
	
	
	
	

}
