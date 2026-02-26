package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.AuthRequest;
import com.loyalt.loyalt.dto.AuthResponse;
import com.loyalt.loyalt.dto.RegisterRequest;
import com.loyalt.loyalt.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;

    }

   /* @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.authenticate(request));

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest){
        authService.register(registerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }*/

    @PostMapping("/me")
    public Object validateToken(Authentication authentication){
        return authentication.getPrincipal();
    }

}
