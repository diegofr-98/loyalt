package com.loyalt.loyalt.security;

import com.loyalt.loyalt.model.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


/*
    public JwtService() {
    }


    public String generateToken(AppUser appUser) {
        return Jwts.builder()
                .setSubject(appUser.getEmail())
                .claim("role", appUser.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }


    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);

    }

    public String extractUserName(String token) {

        return extractClaim(token, claims -> {
            Map<?, ?> userMetadata = claims.get("user_metadata", Map.class);
            if (userMetadata == null) return null;

            Object name = userMetadata.get("name").toString();

            return name != null ? name.toString() : null;
        });
    }

    public String extractRole(String token) {

        return extractClaim(token, claims -> {
            Map<?, ?> userMetadata = claims.get("user_metadata", Map.class);
            if (userMetadata == null) return null;

            Object role = userMetadata.get("role").toString();

            return role != null ? role.toString() : null;
        });
    }

    public String extractPersonId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());

    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    */

}
