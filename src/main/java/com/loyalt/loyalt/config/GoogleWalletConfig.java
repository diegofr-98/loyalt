package com.loyalt.loyalt.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.walletobjects.Walletobjects;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Configuration
public class GoogleWalletConfig {

     @Bean
    public Walletobjects walletobjects() throws Exception {



         GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                 .createScoped(List.of("https://www.googleapis.com/auth/wallet_object.issuer"));

         HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

         return new Walletobjects.Builder(
                 GoogleNetHttpTransport.newTrustedTransport(),
                 JacksonFactory.getDefaultInstance(),
                 requestInitializer
         ).setApplicationName("loyalty-app").build();


     }
}
