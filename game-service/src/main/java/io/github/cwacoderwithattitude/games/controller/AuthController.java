package io.github.cwacoderwithattitude.games.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.cwacoderwithattitude.games.model.User;
import io.github.cwacoderwithattitude.games.repository.UserRepository;
import io.github.cwacoderwithattitude.games.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AuthController.class.getName());

    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        logger.info(null == user ? "User is null" : "User is not null");
        Objects.requireNonNull(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateAnswer(userDetails); // generateToken(userDetails);
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        logger.info(null == user ? "User is null" : "User is not null");
        Objects.requireNonNull(user);
        logger.info(null == user ? "User is null" : "User is not null");
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Error: Username is already taken!";
        }
        // Create new user's account
        User newUser = new User(
                null,
                user.getUsername(),
                encoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return "User registered successfully!";
    }

    private String generateAnswer(UserDetails userDetails) {
        logger.info("Generating token for user: " + userDetails.getUsername());
        String json = String.format("{  \"username\": \"%s\", \"token\": \"%s\" }",
                userDetails.getUsername(),
                generateToken(userDetails));

        return json;
    }

    private String generateToken(UserDetails userDetails) {
        return jwtUtils.generateToken(userDetails.getUsername());
    }
}
