package com.loyalt.loyalt.service;

import com.loyalt.loyalt.dto.AuthRequest;
import com.loyalt.loyalt.dto.AuthResponse;
import com.loyalt.loyalt.dto.RegisterRequest;
import com.loyalt.loyalt.dto.UserInfoResponse;
import com.loyalt.loyalt.exception.InvalidEmailOrPasswordException;
import com.loyalt.loyalt.exception.UserAlreadyExistsException;
import com.loyalt.loyalt.model.entity.AppUser;
import com.loyalt.loyalt.model.enums.Role;
import com.loyalt.loyalt.repository.AppUserRepository;
import com.loyalt.loyalt.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new UserAlreadyExistsException( "Invalid email or password");
        }

        AppUser user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        long expiration = 1000 * 60 * 60;
        UserInfoResponse userInfo = new UserInfoResponse(user.getId(), user.getEmail(), user.getRole());

        return new AuthResponse(token, user.getRole().name(), "Bearer", expiration , userInfo);
    }

    public void register(RegisterRequest registerRequest) {
        if (appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        AppUser user = new AppUser();

        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.BUSINESS);
        appUserRepository.save(user);

    }
}
