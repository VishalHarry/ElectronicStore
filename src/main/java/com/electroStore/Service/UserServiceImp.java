package com.electroStore.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.query.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.UserDto;
import com.electroStore.Entities.User;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Helper.HelperFun;
import com.electroStore.Repositories.UserRepo;
@Service
public class UserServiceImp  implements UserService{
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ImageUploadService imageUploadService;

	@Override
	public UserDto createUser(UserDto userDto) {
		String userId=UUID.randomUUID().toString();
		userDto.setUserId(userId);
		User user=modelMapper.map(userDto, User.class);
		
		User savedUser=userRepo.save(user);
		
		
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String id) throws Exception {
		User savedUser=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundExceptions("User not found with this id !!"));
		User user=modelMapper.map(userDto, User.class);
		savedUser.setName(user.getName());
		savedUser.setPassword(user.getPassword());
		savedUser.setAbout(user.getAbout());
		savedUser.setGender(user.getGender());
		savedUser.setImageUrl(user.getImageUrl());
		
		    userRepo.save(savedUser);

		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String id) throws Exception {
		User savedUser=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundExceptions("User not found with this id !!"));
		 if (savedUser.getImageUrl() != null) {
		        String publicId = imageUploadService.extractPublicId(savedUser.getImageUrl());
		        imageUploadService.deleteFile(publicId);
		 }
		userRepo.delete(savedUser);
		
	}

	@Override
	public PagableResponse<UserDto> getallUsers( int pageNumber, int pageSize,String sortBy,String sortOrder) {
		  Sort sort=(sortOrder.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
	       org.springframework.data.domain.Page<User> page=userRepo.findAll(pageable);
	       
	      PagableResponse<UserDto> resp= HelperFun.getPageResponse(page, UserDto.class,modelMapper);
		    return resp ;
	}

	@Override
	public UserDto getUserById(String id) {
		User savedUser=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundExceptions("User not found with this id !!"));
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
	    User savedUser = userRepo.findByEmailIgnoreCase(email);

	    if (savedUser == null) {
	        throw new RuntimeException("User not found with email: " + email);
	    }

	    return modelMapper.map(savedUser, UserDto.class);
	}


	

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users=userRepo.findByNameContaining(keyword);
		 List<UserDto> userDtos= users.stream().map(user->modelMapper.map(user, UserDto.class)).toList();
		
		return userDtos;
	}

	@Override
	public UserDto uploadUserImage(String userId, MultipartFile file) throws Exception {
		  User user = userRepo.findById(userId)
		            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

		    // upload image to cloudinary
		    String imageUrl = imageUploadService.imageUploadService(file);

		    // save url in user
		    user.setImageUrl(imageUrl);
		    userRepo.save(user);

		    return modelMapper.map(user, UserDto.class); 
	}
	
	@Override
	public UserDto updateUserImage(String userId, MultipartFile file) throws Exception {
	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

	    // 🔴 Agar user ke paas pehle se image hai, toh delete kar do
	    if (user.getImageUrl() != null) {
	        String publicId = imageUploadService.extractPublicId(user.getImageUrl()); 
	        imageUploadService.deleteFile(publicId);
	    }

	    // Upload new image
	    String newImageUrl = imageUploadService.imageUploadService(file);
	    user.setImageUrl(newImageUrl);
	    userRepo.save(user);

	    return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto deleteUserImage(String userId) throws Exception {
	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

	    if (user.getImageUrl() == null) {
	        throw new RuntimeException("User does not have an image to delete.");
	    }

	    String publicId = imageUploadService.extractPublicId(user.getImageUrl());
	    imageUploadService.deleteFile(publicId);

	    user.setImageUrl(null);
	    userRepo.save(user);

	    return modelMapper.map(user, UserDto.class);
	}

}
