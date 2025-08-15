package com.banking.securebankingapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component // Makes this filter a Spring Bean so it can be injected into SecurityConfig
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}") // Inject JWT secret key from application.properties
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Get the Authorization header from the request
        String header = request.getHeader("Authorization");

        // Check if token is present and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Extract token by removing "Bearer "

            try {
                // ✅ Correct JJWT parsing with parserBuilder()
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))) // Secret key for verification
                        .build()
                        .parseClaimsJws(token) // Parse and verify the JWT
                        .getBody(); // Extract claims (payload)

                // Extract username from token
                String username = claims.getSubject();

                // Extract roles from token
                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);

                // Create an Authentication object with roles
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username, // Principal (user identity)
                        null, // No password here
                        roles.stream()
                                .map(SimpleGrantedAuthority::new) // Convert roles to Spring Security authorities
                                .collect(Collectors.toList())
                );

                // Store authentication in SecurityContext so Spring Security can use it
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Token is invalid or expired → clear context
                SecurityContextHolder.clearContext();
            }
        }

        // Continue with the next filter
        filterChain.doFilter(request, response);
    }
}
