package com.electroStore.Security;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {

    private static final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000; // 5 hours
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // ✅ 1. Extract username from token
    public String getUserNameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    // ✅ 2. Extract roles (as comma-separated string) from token
    public String getRolesFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("roles"); // stored as a single comma-separated string
    }

    // ✅ 3. Generic claims extractor
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // ✅ 4. Extract all claims
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    // ✅ 5. Check if token is expired
    public boolean isTokenExpire(String token) {
        final Date expiration = getClaimsFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // ✅ 6. Generate JWT with roles
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Extract roles from UserDetails
        String roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        claims.put("roles", roles); // store roles in JWT payload

        return doGenerateToken(claims, userDetails.getUsername());
    }

    // ✅ 7. Internal token builder
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }

    // ✅ 8. Validate JWT token
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }
}
