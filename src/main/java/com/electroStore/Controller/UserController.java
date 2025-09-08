package com.electroStore.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.UserDto;
import com.electroStore.Entities.User;
import com.electroStore.Helper.Messageresponce;
import com.electroStore.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		
		UserDto userDtos=userService.createUser(userDto);
		
		return new ResponseEntity<UserDto>(userDtos,HttpStatus.CREATED);
		
	}
	

	@PutMapping("/update/{id}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable String id) throws Exception{
		
		UserDto userDtos=userService.updateUser(userDto,id);
		
		return new ResponseEntity<UserDto>(userDtos,HttpStatus.OK);
		
	}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Messageresponce> deleteUser(@PathVariable String id) throws Exception{
		
		userService.deleteUser(id);
		Messageresponce response=new Messageresponce();
		response.setMessage("Delete User Sucessfully!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);
		
		return new ResponseEntity<Messageresponce>(response,HttpStatus.OK); 
		
	}
	
	@GetMapping("/allUsers")
	public ResponseEntity<PagableResponse<UserDto>> getAllUser(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
			                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			                                        @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
		                                            @RequestParam(value = "sortOrder",defaultValue = "ASC",required = false) String sortOrder) throws Exception{
		                                            
		
		PagableResponse<UserDto> users=userService.getallUsers(pageNumber, pageSize,sortBy,sortOrder);
		
		return new ResponseEntity<PagableResponse<UserDto>>(users,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String id) throws Exception{
		
		UserDto users=userService.getUserById(id);
		
		return new ResponseEntity<UserDto>(users,HttpStatus.OK);
		
	}
	
	@GetMapping("/email/{email:.+}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) throws Exception{
		
		UserDto users=userService.getUserByEmail(email);
		
		return new ResponseEntity<UserDto>(users,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String  keyword) throws Exception{
		
		List<UserDto> users=userService.searchUser(keyword);
		
		return new ResponseEntity<List<UserDto>>(users,HttpStatus.OK);
		
	}
	
	@PostMapping("/upload-image/{id}")
	public ResponseEntity<UserDto> uploadUserImage(
	        @PathVariable String id,
	        @RequestParam("file") MultipartFile file) throws Exception {

	    UserDto updatedUser = userService.uploadUserImage(id, file);
	    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	
	@PutMapping("/update-image/{id}")
	public ResponseEntity<UserDto> updateUserImage(
	        @PathVariable String id,
	        @RequestParam("file") MultipartFile file) throws Exception {

	    UserDto updatedUser = userService.updateUserImage(id, file);
	    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	// Delete Image
	@DeleteMapping("/delete-image/{id}")
	public ResponseEntity<UserDto> deleteUserImage(@PathVariable String id) throws Exception {
	    UserDto updatedUser = userService.deleteUserImage(id);
	    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	

}
