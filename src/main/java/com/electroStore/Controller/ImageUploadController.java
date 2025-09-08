package com.electroStore.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.Service.ImageUploadService;



@RestController
public class ImageUploadController {
	
	@Autowired
	private ImageUploadService uploadImage;
	
	@PostMapping("/api/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
	    System.out.println("File received: " + file.getOriginalFilename()); // ✅ Debug
	   

	    try {
	        String imageUrl = uploadImage.imageUploadService(file);
	        return ResponseEntity.ok(imageUrl);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Upload File: " + e.getMessage());
	    }
	}
	@DeleteMapping("/api/delete")
	public ResponseEntity<?> deleteImage(@RequestParam String publicId){
		
		try {
			uploadImage.deleteFile(publicId);
			return ResponseEntity.ok("Image Delete sucessfully!!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Delete failed: " + e.getMessage());
		}
		
	}

}
