package com.loyalt.loyalt.service;

import com.loyalt.loyalt.dto.AuthRequest;
import com.loyalt.loyalt.dto.AuthResponse;
import com.loyalt.loyalt.dto.RegisterRequest;
import com.loyalt.loyalt.model.entity.AppUser;
import com.loyalt.loyalt.model.enums.Role;
import com.loyalt.loyalt.repository.AppUserRepository;
import com.loyalt.loyalt.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        AppUser user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);

    }

    public void register(RegisterRequest registerRequest){
        if(appUserRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User already exists");
        }
        AppUser user = new AppUser();

        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.BUSINESS);
        appUserRepository.save(user);

    }
}
