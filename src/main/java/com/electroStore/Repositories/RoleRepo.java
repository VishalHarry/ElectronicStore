package com.electroStore.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudinary.provisioning.Account.Role;
import com.electroStore.Entities.User;

@Repository
public interface RoleRepo extends JpaRepository<com.electroStore.Entities.Role, Long> {
	boolean existsByRoleName(String name);
	Optional<com.electroStore.Entities.Role> findByRoleName(String roleName);

	

}
