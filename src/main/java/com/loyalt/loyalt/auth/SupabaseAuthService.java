package com.loyalt.loyalt.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SupabaseAuthService {
    private final WebClient webClient;
    private final String publishableKey;

    public SupabaseAuthService(@Value("${supabase.url}") String supabaseUrl, @Value("${supabase.publishable-key}") String publishableKey){
        this.publishableKey = publishableKey;
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("apiKey", publishableKey)
                .build();
    }

    public Map validateToken(String token) {
        return webClient.get()
                .uri("/auth/v1/user")
                .header("Authorization", "Bearer "+token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

    }



}
