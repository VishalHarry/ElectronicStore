package com.electroStore.Controller;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.ProductDto;
import com.electroStore.Helper.Messageresponce;
import com.electroStore.Service.ProductService;

import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {
        ProductDto resp = productService.createProduct(dto);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto dto, @PathVariable String id) {
        ProductDto resp = productService.updateProduct(dto, id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Messageresponce> deleteProduct(@PathVariable String id) throws Exception {
        productService.deleteproduct(id);

        Messageresponce resp = new Messageresponce();
        resp.setMessage("Product deleted successfully!");
        resp.setSuccess(true);
        resp.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

   
    @GetMapping("/all")
    public ResponseEntity<PagableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {

        PagableResponse<ProductDto> resp = productService.getAllProduct(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String id) {
        ProductDto resp = productService.getSingleProduct(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PagableResponse<ProductDto>> searchProduct(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {

        PagableResponse<ProductDto> resp = productService.searchProduct(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    
    // ✅ Upload Product Image
    @PostMapping("/upload/{id}")
    public ResponseEntity<ProductDto> uploadImage(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file) throws IOException {

        ProductDto resp = productService.uploadImage(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // ✅ Update Product Image
    @PutMapping("/update-image/{id}")
    public ResponseEntity<ProductDto> updateImage(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file) throws Exception {

        ProductDto resp = productService.updateImage(id, file);
        return ResponseEntity.ok(resp);
    }

    // ✅ Delete Product Image
    @DeleteMapping("/delete-image/{id}")
    public ResponseEntity<Messageresponce> deleteImage(@PathVariable String id) throws Exception {
         productService.deleteImage(id);

        Messageresponce resp = new Messageresponce();
        resp.setMessage("Image deleted successfully!!");
        resp.setStatus(HttpStatus.OK);
        resp.setSuccess(true);

        return ResponseEntity.ok(resp);
    }
    
    @PostMapping("/create/category/{id}")
    public ResponseEntity<ProductDto> createProductwithCategory(@Valid @RequestBody ProductDto dto,@PathVariable String id) {
        ProductDto resp = productService.createProductWithCategory(dto,id);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }
    
    @PutMapping("/update/category/{id}/product/{productId}")
    public ResponseEntity<ProductDto> udateProductCAtegory(@PathVariable String id,@PathVariable String productId) {
        ProductDto resp = productService.updateCategoryOFProductDto(productId, id);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }
}
