package com.electroStore.Controller;

import java.io.IOException;
import java.net.http.HttpResponse.ResponseInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.CategoryDto;
import com.electroStore.DTOs.PagableResponse;
import com.electroStore.Helper.Messageresponce;
import com.electroStore.Service.CategoryService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/create-Category")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto
			                                        ) throws IOException{
		
		CategoryDto resp=categoryService.create(dto);
		
		return new ResponseEntity<CategoryDto>(resp,HttpStatus.CREATED);
		
		
	}
	
	@PutMapping("/update-Category/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto dto,@PathVariable String id
			                                          ) throws IOException{
		
		CategoryDto resp=categoryService.updateCategory(dto, id);
		
		return new ResponseEntity<CategoryDto>(resp,HttpStatus.OK);
		
		
	}
	
	
	@DeleteMapping("/delete-Category/{id}")
	public ResponseEntity<Messageresponce> deleteCategory(@PathVariable String id) throws Exception{
		
        categoryService.deleteCategory(id);
        Messageresponce resp=new Messageresponce();
        resp.setMessage("Category Delete Successfully!!");
        resp.setStatus(HttpStatus.OK);
        resp.setSuccess(true);
		
		return new ResponseEntity<Messageresponce>(resp,HttpStatus.OK);
		
		
	}
	
	@GetMapping("/all-Category")
	public ResponseEntity<PagableResponse<CategoryDto>> getAllCategory(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = "ASC",required = false) String sortOrder){
		
		PagableResponse<CategoryDto> resp=categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<PagableResponse<CategoryDto>>(resp,HttpStatus.OK);
		
		
	}
	
	
	@GetMapping("/single-Category/{id}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String id){
		
		CategoryDto resp=categoryService.getSingleCategory(id);
		
		return new ResponseEntity<CategoryDto>(resp,HttpStatus.OK);
		
		
	}
	
	
	 // ✅ Upload Category Image
    @PostMapping("/upload/{id}")
    public ResponseEntity<CategoryDto> uploadImage(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file) throws IOException {

        CategoryDto resp = categoryService.uploadImage(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // ✅ Update Category Image
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateImage(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file) throws Exception {

        CategoryDto resp = categoryService.updateImage(id, file);
        return ResponseEntity.ok(resp);
    }

    // ✅ Delete Category Image
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Messageresponce> deleteImage(@PathVariable String id) throws Exception {
        CategoryDto updatedCategory = categoryService.deleteImage(id);

        Messageresponce resp=new Messageresponce();
        resp.setMessage("Image deleted successfully!!");
        resp.setStatus(HttpStatus.OK);
        resp.setSuccess(true);
        

        return ResponseEntity.ok(resp);
    }
	
	

}
