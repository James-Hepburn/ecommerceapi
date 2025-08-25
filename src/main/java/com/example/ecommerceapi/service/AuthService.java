package com.example.ecommerceapi.service;

import com.example.ecommerceapi.model.User;
import com.example.ecommerceapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public String register (User user) {
        user.setPassword (this.passwordEncoder.encode (user.getPassword ()));
        this.userRepository.save (user);
        return this.jwtService.generateToken (user.getUsername ());
    }

    public String login (String username, String password) {
        this.authenticationManager.authenticate (new UsernamePasswordAuthenticationToken (username, password));
        User user = this.userRepository.findByUsername (username).orElseThrow (() -> new IllegalArgumentException ("User not found"));
        return this.jwtService.generateToken (user.getUsername ());
    }
}
