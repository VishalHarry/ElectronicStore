package com.electroStore.Service;

import java.util.List;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.ProductDto;
import com.electroStore.Helper.Messageresponce;

public interface ProductService {
	
	
	public ProductDto createProduct(ProductDto dto);
	
	
	public ProductDto updateProduct(ProductDto dto,String id);
	
	public void deleteproduct(String id);
	
	public PagableResponse<ProductDto> getAllProduct( int pageNumber, int pageSize, String sortBy, String sortOrder);
	
	public ProductDto getSingleProduct(String id);
	
	public PagableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);
	
	
	
	
	
	
	
	

}
