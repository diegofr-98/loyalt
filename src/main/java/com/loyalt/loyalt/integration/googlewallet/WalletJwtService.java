package com.loyalt.loyalt.integration.googlewallet;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.loyalt.loyalt.exception.BadRequestException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletAuthenticationException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;
import java.util.Map;

@Service
public class WalletJwtService {

    private final GoogleCredentials credentials;

    public WalletJwtService() throws IOException {
        try {
            this.credentials = GoogleCredentials.getApplicationDefault();
        } catch (IOException e) {
            throw new GoogleWalletAuthenticationException(
                    "Failed to load Google credentials",
                    e
            );
        }
    }

    public String createSaveLink(String objectId) {

        if (objectId == null || objectId.isBlank()) {
            throw new BadRequestException("objectId cannot be null");
        }

        try {
            if (!(credentials instanceof ServiceAccountCredentials serviceAccount)) {
                throw new GoogleWalletAuthenticationException(
                        "Invalid Google credentials type",
                        null
                );
            }

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

        } catch (JWTCreationException e) {
            throw new GoogleWalletException("Error generating Google Wallet JWT", e);
        } catch (Exception e){
            throw new GoogleWalletException("Unexpected error generating Add to Wallet link");
        }
    }
}
