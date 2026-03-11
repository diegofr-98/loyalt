package com.loyalt.loyalt.integration.googlewallet;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;
import java.util.Map;

@Service
public class WalletJwtService {

    private final GoogleCredentials credentials;

    public WalletJwtService() throws IOException {
        this.credentials = GoogleCredentials.getApplicationDefault();
    }

    public String createSaveLink(String objectId) {

        try {

            ServiceAccountCredentials serviceAccount =
                    (ServiceAccountCredentials) credentials;

            String serviceAccountEmail = serviceAccount.getClientEmail();
            RSAPrivateKey privateKey = (RSAPrivateKey) serviceAccount.getPrivateKey();

            Map<String, Object> payload = Map.of(
                    "loyaltyObjects",
                    List.of(Map.of("id", objectId))
            );

            Algorithm algorithm = Algorithm.RSA256(null, privateKey);

            String jwt = JWT.create()
                    .withIssuer(serviceAccountEmail)
                    .withAudience("google")
                    .withClaim("typ", "savetowallet")
                    .withClaim("payload", payload)
                    .sign(algorithm);

            return "https://pay.google.com/gp/v/save/" + jwt;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Add to Wallet link", e);
        }
    }
}
