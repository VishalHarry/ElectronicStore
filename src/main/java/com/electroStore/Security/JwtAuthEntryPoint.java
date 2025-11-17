package com.electroStore.Security;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Set status code
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 (better than 403 here)

        // Set response type
        response.setContentType("application/json");

        // Send JSON error response
        PrintWriter writer = response.getWriter();
        writer.write("{\"error\": \"Unauthorized access - " + authException.getMessage() + "\"}");
        writer.flush();
        writer.close();
    }
}
