package com.loyalt.loyalt.integration.googlewallet;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletAuthenticationException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletAuthorizationException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class GoogleWalletExceptionTranslator {

    public RuntimeException translate(GoogleJsonResponseException e) {

        int status = e.getStatusCode();

        if (status == 401) {
            return new GoogleWalletAuthenticationException(
                    "No autenticado con Google Wallet", e);
        }

        if (status == 403) {
            return new GoogleWalletAuthorizationException(
                    "Sin permisos para Google Wallet", e);
        }

        if (status == 404) {
            return new GoogleWalletResourceNotFoundException(
                    "Recurso no encontrado en Google Wallet", e);
        }

        String message = e.getDetails() != null
                ? e.getDetails().getMessage()
                : e.getMessage();

        return new GoogleWalletException(
                "Error de Google Wallet: " + message,
                e
        );
    }
}