package com.electroStore.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.UserDto;

public interface UserService {
	
	
	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto,String id) throws Exception;
	
	
	void deleteUser(String id) throws Exception;
	
	PagableResponse<UserDto> getallUsers( int pageNumber, int pageSize,String sortBy,String sortOrder);
	
	UserDto getUserById(String id);
	
	UserDto getUserByEmail(String email);
	
	
	List<UserDto> searchUser(String keyword);
	
	UserDto uploadUserImage(String userId, MultipartFile file) throws Exception;
	
	UserDto updateUserImage(String userId, MultipartFile file) throws Exception;

	UserDto deleteUserImage(String userId) throws Exception;


}
