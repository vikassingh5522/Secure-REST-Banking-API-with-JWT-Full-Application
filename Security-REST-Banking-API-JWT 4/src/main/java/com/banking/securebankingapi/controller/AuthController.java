package com.banking.securebankingapi.controller;

import com.banking.securebankingapi.entity.User;
import com.banking.securebankingapi.security.JwtUtil;
import com.banking.securebankingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController  // Marks this class as a REST API controller
@RequestMapping("/api/auth")  // Base URL path for authentication-related endpoints
public class AuthController {

    @Autowired
    private UserService userService; // Service to handle user registration & retrieval

    @Autowired
    private JwtUtil jwtUtil; // Utility to generate & validate JWT tokens

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // For securely hashing & verifying passwords

    /**
     * Register a new user in the system.
     *
     * @param user - User object from request body (JSON → User)
     * @return ResponseEntity containing the saved user object
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Calls UserService to register and save the user in the DB
        return ResponseEntity.ok(userService.register(user));
    }

    /**
     * Authenticate user and return JWT token if credentials are valid.
     *
     * @param loginUser - User object containing username and password from the request
     * @return ResponseEntity containing a JWT token or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginUser) {
        // Retrieve user from DB using username
        User user = userService.findByUsername(loginUser.getUsername());

        // If user exists AND password matches the hashed password in DB
        if (user != null && passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            // Generate JWT token with username & role
            String token = jwtUtil.generateToken(user.getUsername(), List.of(user.getRole()));

            // Return token as JSON → { "token": "xxxxxx" }
            return ResponseEntity.ok(Map.of("token", token));
        }

        // If authentication fails, return HTTP 401 Unauthorized
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
