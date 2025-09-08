package com.electroStore.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if(value.isBlank()) {
			return false;
		}else {
			return true;
		}
		
		
	}

}
