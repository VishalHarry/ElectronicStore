package com.electroStore.Helper;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Messageresponce {
	
	private String message;
	private boolean success;
	private HttpStatus status;

}
