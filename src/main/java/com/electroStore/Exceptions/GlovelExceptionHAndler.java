package com.electroStore.Exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.electroStore.Helper.Messageresponce;

@RestControllerAdvice
public class GlovelExceptionHAndler {
	@ExceptionHandler(ResourceNotFoundExceptions.class)
	public ResponseEntity<Messageresponce>  resourceNotFoundExceptionsHandler(ResourceNotFoundExceptions ex){
		
		Messageresponce message=new Messageresponce();
		message.setMessage(ex.getMessage());
		message.setStatus(HttpStatus.NOT_FOUND);
		message.setSuccess(false);
		
		
		
		return new ResponseEntity<Messageresponce>(message,HttpStatus.OK);  
		
	}


	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

	        Map<String, String> errors = new HashMap<>();

	        ex.getBindingResult().getAllErrors().forEach((error) -> {
	            String fieldName = ((FieldError) error).getField();
	            String errorMessage = error.getDefaultMessage();
	            errors.put(fieldName, errorMessage);
	        });

	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }

	

}
