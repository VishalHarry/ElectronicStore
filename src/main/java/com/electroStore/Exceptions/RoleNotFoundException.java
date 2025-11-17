package com.electroStore.Exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;


public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException(String role) {
        super("Role '" + role + "' not found");
    }

}
