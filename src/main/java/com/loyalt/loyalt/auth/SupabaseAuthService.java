package com.loyalt.loyalt.auth;

import com.loyalt.loyalt.config.SupabaseProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SupabaseAuthService {
    private final WebClient webClient;
    private final SupabaseProperties supabaseProperties;

    public SupabaseAuthService(SupabaseProperties supabaseProperties){
        this.supabaseProperties = supabaseProperties;
        this.webClient = WebClient.builder()
                .baseUrl(supabaseProperties.getUrl())
                .defaultHeader("apiKey", supabaseProperties.getPublishableKey())
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
