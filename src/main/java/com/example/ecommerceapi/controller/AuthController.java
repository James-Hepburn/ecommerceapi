package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.dto.LoginRequest;
import com.example.ecommerceapi.model.User;
import com.example.ecommerceapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity <String> register (@RequestBody User user) {
        String token = this.authService.register (user);
        return ResponseEntity.ok (token);
    }

    @PostMapping("/login")
    public ResponseEntity <String> register (@RequestBody LoginRequest request) {
        String token = this.authService.login (request.getUsername (), request.getPassword ());
        return ResponseEntity.ok (token);
    }
}
