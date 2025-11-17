package com.electroStore.Security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        // ✅ Step 1: Check header and extract JWT token
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try {
                username = jwtHelper.getUserNameFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("⚠️ JWT Token has expired");
            } catch (MalformedJwtException e) {
                System.out.println("❌ Invalid JWT Token format");
            } catch (SignatureException e) {
                System.out.println("❌ JWT Signature does not match");
            }
        } 

        // ✅ Step 2: Validate token and set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from DB (optional if token contains roles)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Extract roles directly from token
            String rolesStr = jwtHelper.getRolesFromToken(token);
            Collection<SimpleGrantedAuthority> authorities = Arrays.stream(rolesStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            // Check token validity
            if (!jwtHelper.isTokenExpire(token)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("⚠️ JWT Token is expired, cannot set authentication");
            }
        }

        // ✅ Step 3: Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
