package com.electroStore.Exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFoundExceptions extends RuntimeException {

	public ResourceNotFoundExceptions() {
		super("Resource NOt Found !!");
		
	}

	public ResourceNotFoundExceptions(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundExceptions(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundExceptions(String message) {
		super(message);
		
	}

	public ResourceNotFoundExceptions(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
