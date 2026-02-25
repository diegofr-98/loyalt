package com.loyalt.loyalt.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class HelloWorld {
    @GetMapping("/secure")
    public Map<String, Object> secureEndpoint(Authentication authentication){

        if(authentication == null){
            return Map.of("auth", "NULL");}

       return  Map.of("authenticated", authentication.isAuthenticated(),
                "user", authentication.getName(),
                "authorities", authentication.getAuthorities());



    }


}
