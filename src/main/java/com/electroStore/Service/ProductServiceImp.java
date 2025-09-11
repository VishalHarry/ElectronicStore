package com.electroStore.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.ProductDto;
import com.electroStore.Entities.Product;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Helper.HelperFun;
import com.electroStore.Repositories.ProductRepo;

@Service
public class ProductServiceImp implements ProductService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductRepo repo;

	@Override
	public ProductDto createProduct(ProductDto dto) {
		
		String productId=UUID.randomUUID().toString();
		dto.setId(productId);
		
		Product product=modelMapper.map(dto, Product.class);
		repo.save(product);
		
		
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto dto, String id) {
	 
	    Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with this id: " + id));

	    
	    savedProduct.setTitle(dto.getTitle());
	    savedProduct.setDescription(dto.getDescription());
	    savedProduct.setPrice(dto.getPrice());
	    savedProduct.setDicountPric(dto.getDicountPric());
	    savedProduct.setQuantity(dto.getQuantity());
	    savedProduct.setLive(dto.getLive());
	    savedProduct.setStock(dto.getStock());


	    if (dto.getAddedDate() != null) {
	        savedProduct.setAddedDate(dto.getAddedDate());
	    }

	    Product updatedProduct = repo.save(savedProduct);
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}


	@Override
	public void deleteproduct(String id) {
		
		Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with this id: " + id));
		
		repo.delete(savedProduct);
	}

	@Override
	public PagableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortOrder) {
	Sort sort=(sortOrder.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
	Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
	Page<Product> products=repo.findAll(pageable);
	PagableResponse<ProductDto> resp=HelperFun.getPageResponse(products, ProductDto.class, modelMapper);
		return resp;
	}

	@Override
	public ProductDto getSingleProduct(String id) {
		Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with this id: " + id));
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public PagableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy,
			String sortOrder) {
		
		Sort sort=(sortOrder.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> products=repo.findByTitleContainingIgnoreCase(keyword,pageable);
		
		PagableResponse<ProductDto> resp=HelperFun.getPageResponse(products, ProductDto.class, modelMapper);
			return resp;
		
		
	}

}
