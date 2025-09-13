package com.electroStore.Service;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.CategoryDto;
import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.ProductDto;
import com.electroStore.Entities.Category;
import com.electroStore.Entities.Product;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Helper.HelperFun;
import com.electroStore.Repositories.CategoryRepo;
import com.electroStore.Repositories.ProductRepo;

@Service
public class ProductServiceImp implements ProductService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductRepo repo;
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ImageUploadService imageUploadService;

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
	public void deleteproduct(String id) throws Exception {
		
		Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with this id: " + id));
		// 🔴 Agar image hai toh pehle cloudinary se delete karo
	    if (savedProduct.getImage() != null) {
	        String publicId = imageUploadService.extractPublicId(savedProduct.getImage());
	        imageUploadService.deleteFile(publicId);
	    }
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

	@Override
	public ProductDto uploadImage(String id, MultipartFile file) throws IOException {
	    Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with id: " + id));

	    if (file.isEmpty()) {
	        throw new IllegalArgumentException("File cannot be empty.");
	    }

	    // Upload new image
	    String image = imageUploadService.imageUploadService(file);
	    savedProduct.setImage(image);

	    Product updatedProduct = repo.save(savedProduct);
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}


	@Override
	public ProductDto updateImage(String id, MultipartFile file) throws IOException, Exception {
	    Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with id: " + id));

	    if (file.isEmpty()) {
	        throw new IllegalArgumentException("File cannot be empty.");
	    }

	    // 🔴 If product already has an image, delete the old one from cloud
	    if (savedProduct.getImage() != null && !savedProduct.getImage().contains("dummyimage")) {
	        String publicId = imageUploadService.extractPublicId(savedProduct.getImage());
	        imageUploadService.deleteFile(publicId);
	    }

	    // Upload new image
	    String image = imageUploadService.imageUploadService(file);
	    savedProduct.setImage(image);

	    Product updatedProduct = repo.save(savedProduct);
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}


	@Override
	public ProductDto deleteImage(String id) throws IOException, Exception {
	    Product savedProduct = repo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with id: " + id));

	    if (savedProduct.getImage() == null || savedProduct.getImage().contains("dummyimage")) {
	        throw new RuntimeException("Product does not have a valid image to delete.");
	    }

	    // Delete from cloud
	    String publicId = imageUploadService.extractPublicId(savedProduct.getImage());
	    imageUploadService.deleteFile(publicId);

	    // Replace with dummy placeholder
	    savedProduct.setImage("https://dummyimage.com/600x400/cccccc/000000&text=No+Image");

	    Product updatedProduct = repo.save(savedProduct);
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public ProductDto createProductWithCategory(ProductDto dto, String categoryId) {
		Category category=categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundExceptions("The category not found with this id:" + categoryId) );
		
		String productId=UUID.randomUUID().toString();
		dto.setId(productId);
		
		
		Product product=modelMapper.map(dto, Product.class);
		product.setCategory(category);
		repo.save(product);
		
		
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public ProductDto updateCategoryOFProductDto(String productId, String categoryId) {
		  
	    Product product = repo.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Product not found with id: " + productId));

	  
	    Category category = categoryRepo.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("Category not found with id: " + categoryId));

	
	    product.setCategory(category);

	  
	    Product updatedProduct = repo.save(product);

	    
	    return modelMapper.map(updatedProduct, ProductDto.class);
	}


}
