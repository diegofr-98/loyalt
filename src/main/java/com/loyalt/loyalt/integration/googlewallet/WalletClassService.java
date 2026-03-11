package com.loyalt.loyalt.integration.googlewallet;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.walletobjects.model.Image;
import com.google.api.services.walletobjects.model.ImageUri;
import com.google.api.services.walletobjects.model.LoyaltyClass;
import com.loyalt.loyalt.exception.wallet.*;
import com.loyalt.loyalt.exception.LoyaltyClassAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class WalletClassService {
    private static final Logger logger = LoggerFactory.getLogger(WalletClassService.class);
    private final WalletClient walletClient;
    private final GoogleWalletExceptionTranslator exceptionTranslator;


    public WalletClassService(WalletClient walletClient, GoogleWalletExceptionTranslator exceptionTranslator) {
        this.walletClient = walletClient;
        this.exceptionTranslator = exceptionTranslator;

    }

    public String createLoyaltyClass(
            String googleClassId,
            String businessName,
            String programName,
            String logoURL) {

        try {

            if (loyaltyClassExists(googleClassId)) {
                throw new LoyaltyClassAlreadyExistsException(
                        "La LoyaltyClass ya existe: " + googleClassId);
            }

            LoyaltyClass loyaltyClass = new LoyaltyClass()
                    .setId(googleClassId)
                    .setIssuerName(businessName)
                    .setProgramName(programName)
                    .setHexBackgroundColor("#7387E7")
                    .setReviewStatus("UNDER_REVIEW")
                    .setProgramLogo(
                            new Image().setSourceUri(
                                    new ImageUri().setUri(logoURL)));

            LoyaltyClass response = walletClient
                    .getLoyaltyClass()
                    .insert(loyaltyClass)
                    .execute();

            logger.info("LoyaltyClass creada en Google Wallet: {}", response.getId());

            return response.getId();

        } catch (GoogleJsonResponseException e) {
            logger.error(
                    "Error creando LoyaltyClass {} en Google Wallet. Status: {}",
                    googleClassId,
                    e
            );
            throw exceptionTranslator.translate(e);
        } catch (IOException e) {

            throw new GoogleWalletCommunicationException(
                    "Error de comunicación con Google Wallet", e);
        }
    }

    private boolean loyaltyClassExists(String classId) {

        try {

            walletClient
                    .getLoyaltyClass()
                    .get(classId)
                    .execute();

            return true;

        } catch (GoogleJsonResponseException e) {

            int status = e.getStatusCode();

            if (status == 404) {
                return false;
            }

            logger.warn(
                    "Error verificando LoyaltyClass {} en Google Wallet. Status: {}",
                    classId,
                    status
            );
            throw exceptionTranslator.translate(e);



        } catch (IOException e) {
            throw new GoogleWalletCommunicationException(
                    "Error de comunicación con Google Wallet",
                    e
            );
        }
    }

}