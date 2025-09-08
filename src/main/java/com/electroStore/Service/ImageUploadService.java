package com.electroStore.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {
	@Autowired
	private Cloudinary cloudinary;

	public String imageUploadService(MultipartFile file) throws IOException {
		Map uploadResult=cloudinary.uploader().upload(file.getBytes(),ObjectUtils.emptyMap());
		
		return uploadResult.get("secure_url").toString();
	}

	public void deleteFile(String publicId) throws Exception {
		
		try {
			cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
			System.out.println("Image deleted successfully: " + publicId);
		} catch (Exception e) {
			System.err.println("Error deleting image: " + e.getMessage());
            throw new Exception("Failed to delete image: " + e.getMessage());
		}
		
	}
	
	public String extractPublicId(String imageUrl) {
	    // Example: https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg
	    // PublicId = "sample"
	    String[] parts = imageUrl.split("/");
	    String fileName = parts[parts.length - 1];
	    return fileName.split("\\.")[0];
	}

	
	
}
