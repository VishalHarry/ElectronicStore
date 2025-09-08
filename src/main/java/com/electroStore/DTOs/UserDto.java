package com.electroStore.DTOs;

import com.electroStore.validate.ImageNameValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private  String userId;
	@Size(min=3,max = 15,message = "Invalied Name !!")
	private  String name;
//	@Email(message = "Invalied User email")
	@Pattern(
		    regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
		    message = "Invalid email format"
		)
	private String email;
	@NotBlank(message = "Password is required")
	private String password;
	@Size(min = 4,max = 6)
	private String gender;
	private String about;
	@ImageNameValid
	private String imageUrl;
	

}
