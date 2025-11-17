package com.electroStore.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    private static final String[] PUBLIC_URLS = {
    	    "/auth/**",
    	    "/v3/api-docs/**",
    	    "/swagger-ui/**",
    	    "/swagger-ui.html",
    	    "/swagger-resources/**",
    	    "/webjars/**"
    	};

    // ✅ Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // ✅ Authentication Provider (for database user authentication)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ✅ Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ✅ Main Security Filter Chain Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    	

        http
            // 🔐 Disable CSRF (since we use JWT)
            .csrf(csrf -> csrf.disable())

            // 🚫 Handle unauthorized access
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))

            // 🧠 Stateless session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth
            		
            		.requestMatchers(PUBLIC_URLS).permitAll()
                // 🛒 PRODUCT endpoints
                .requestMatchers(HttpMethod.POST, "/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()

                // 📦 CATEGORY endpoints
                .requestMatchers(HttpMethod.POST, "/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/category/**").permitAll()

                // 🧍 USER endpoints (signup, login allowed)
                .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/**").permitAll()

                // 🛒 CART endpoints
                .requestMatchers("/cart/**").hasAnyRole("USER", "ADMIN")

                // 📦 ORDER endpoints
                .requestMatchers(HttpMethod.POST, "/order/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/order/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/order/**").hasRole("ADMIN")
                

                // 🔓 Allow auth endpoints (like /auth/login, /auth/register)
                .requestMatchers("/auth/**").permitAll()

                // 🚫 Everything else requires authentication
                .anyRequest().authenticated()
            )

            // ✅ Add JWT Filter before default authentication
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            // ✅ Build configuration
            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
