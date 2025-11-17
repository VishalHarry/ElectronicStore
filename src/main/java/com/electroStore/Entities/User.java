package com.electroStore.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User   {
	@Id
	private  String userId;
	
	
	private  String name;
	
	private String email;
	
	private String password;
	
	private String gender;
	private String about;
	private String imageUrl;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders=new ArrayList<>();
	
	 // Many-to-Many relation with Role
	 @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	    @JoinTable(
	            name = "user_roles",
	            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
	            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId")
	    )
	    private Set<Role> roles = new HashSet<>();
    
    
	
	
	


}
