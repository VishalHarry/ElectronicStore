package com.electroStore;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ecommerce.model.User;
import com.electroStore.Entities.Role;
import com.electroStore.Repositories.RoleRepo;
import com.electroStore.Security.JwtHelper;

@SpringBootApplication
@EnableWebMvc
public class Application {
	


    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

	
	


    
}
