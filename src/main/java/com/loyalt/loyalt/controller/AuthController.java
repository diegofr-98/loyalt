package com.loyalt.loyalt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    @PostMapping("/me")
    public Object validateToken(Authentication authentication){
        return authentication.getPrincipal();
    }

}
