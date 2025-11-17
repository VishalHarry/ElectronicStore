package com.electroStore.DTOs;

import java.util.HashSet;
import java.util.Set;

import com.electroStore.Entities.Role;
import com.electroStore.Entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

	
	    private Long roleId;

	    
	    private String roleName;

	   
}
