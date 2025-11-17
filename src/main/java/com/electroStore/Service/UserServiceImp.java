package com.electroStore.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.query.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.electroStore.DTOs.PagableResponse;
import com.electroStore.DTOs.RoleDto;
import com.electroStore.DTOs.UserDto;
import com.electroStore.Entities.Role;
import com.electroStore.Entities.User;
import com.electroStore.Exceptions.ResourceNotFoundExceptions;
import com.electroStore.Helper.HelperFun;
import com.electroStore.Repositories.RoleRepo;
import com.electroStore.Repositories.UserRepo;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class UserServiceImp  implements UserService{
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ImageUploadService imageUploadService;
	
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	@Transactional
	public UserDto createUser(UserDto userDto) {
	    // 1️⃣ Generate unique userId
	    String userId = UUID.randomUUID().toString();
	    userDto.setUserId(userId);

	    // 2️⃣ Map DTO → Entity
	    User user = modelMapper.map(userDto, User.class);

	    // 3️⃣ Encode password if encoder configured
	    user.setPassword(user.getPassword());

	    // 4️⃣ Handle roles
	    Set<Role> roles = new HashSet<>();

	    if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
	        // ✅ Case 1: Roles provided in JSON like ["ADMIN", "USER"]
	        for (String roleName : userDto.getRoles()) {
	            Role role = roleRepo.findByRoleName(roleName)
	                    .orElseGet(() -> {
	                        // Auto-create role if not found
	                        Role newRole = new Role();
	                        newRole.setRoleName(roleName);
	                        return roleRepo.save(newRole);
	                    });
	            roles.add(role);
	        }
	    } else {
	        // ✅ Case 2: No roles provided → assign default "USER" role
	        Role defaultRole = roleRepo.findByRoleName("USER")
	                .orElseGet(() -> {
	                    Role newRole = new Role();
	                    newRole.setRoleName("USER");
	                    return roleRepo.save(newRole);
	                });
	        roles.add(defaultRole);
	    }

	    user.setRoles(roles);

	    // 5️⃣ Save user
	    User savedUser = userRepo.save(user);

	    // 6️⃣ Convert back to DTO
	    UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);

	    // 7️⃣ Convert role entities → String names for response
	    Set<String> roleNames = savedUser.getRoles()
	            .stream()
	            .map(Role::getRoleName)
	            .collect(Collectors.toSet());

	    savedUserDto.setRoles(roleNames);

	    return savedUserDto;
	}


	@Override
	@Transactional
	public UserDto updateUser(UserDto userDto, String id) throws Exception {
	    // 1️⃣ Find existing user
	    User savedUser = userRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundExceptions("User not found with id: " + id));

	    // 2️⃣ Update simple fields (only if not null)
	    if (userDto.getName() != null) savedUser.setName(userDto.getName());
	    if (userDto.getPassword() != null && !userDto.getPassword().isBlank())
	        savedUser.setPassword(userDto.getPassword());
	    if (userDto.getAbout() != null) savedUser.setAbout(userDto.getAbout());
	    if (userDto.getGender() != null) savedUser.setGender(userDto.getGender());
	    if (userDto.getImageUrl() != null) savedUser.setImageUrl(userDto.getImageUrl());
	    if (userDto.getEmail() != null) savedUser.setEmail(userDto.getEmail());

	    // 3️⃣ Handle role updates (if provided)
	    if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
	        Set<Role> updatedRoles = new HashSet<>();

	        for (String roleName : userDto.getRoles()) {
	            Role role = roleRepo.findByRoleName(roleName)
	                    .orElseGet(() -> {
	                        // Auto-create new role if not found
	                        Role newRole = new Role();
	                        newRole.setRoleName(roleName);
	                        return roleRepo.save(newRole);
	                    });
	            updatedRoles.add(role);
	        }

	        savedUser.setRoles(updatedRoles);
	    }

	    // 4️⃣ Save updated user
	    User updatedUser = userRepo.save(savedUser);

	    // 5️⃣ Convert to DTO
	    UserDto updatedUserDto = modelMapper.map(updatedUser, UserDto.class);

	    // 6️⃣ Convert roles (Set<Role> → Set<String>)
	    Set<String> roleNames = updatedUser.getRoles().stream()
	            .map(Role::getRoleName)
	            .collect(Collectors.toSet());
	    updatedUserDto.setRoles(roleNames);

	    return updatedUserDto;
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
