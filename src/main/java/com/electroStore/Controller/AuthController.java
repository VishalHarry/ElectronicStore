package com.electroStore.Controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.electroStore.DTOs.LoginRequest;
import com.electroStore.DTOs.UserDto;
import com.electroStore.Entities.Role;
import com.electroStore.Entities.User;
import com.electroStore.Repositories.RoleRepo;
import com.electroStore.Repositories.UserRepo;
import com.electroStore.Security.CustomUserDetails;
import com.electroStore.Security.CustomUserDetailsService;
import com.electroStore.Security.JwtHelper;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
   private  CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register User (Signup)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already exists"));
        }

        // 🧩 Convert DTO → Entity
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // encode for security
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageUrl(userDto.getImageUrl());

        // 🧩 Set Default Role
        Role defaultRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        user.getRoles().add(defaultRole);

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "user", userDto.getEmail()
        ));
    }

    // ✅ Login User (Generate JWT)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Step 1️⃣: Authenticate user credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("❌ Invalid username or password!");
        }

        // Step 2️⃣: Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Step 3️⃣: Generate JWT token
        String token = jwtHelper.generateToken(userDetails);

        // Step 4️⃣: Convert roles to simple string list
        var roles = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())  // e.g., "ROLE_ADMIN"
                .collect(Collectors.toList());

        // Step 5️⃣: Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", userDetails.getUsername());
        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }
}
