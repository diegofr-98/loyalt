package com.loyalt.loyalt.integration.googlewallet;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.walletobjects.Walletobjects;
import com.google.api.services.walletobjects.model.Image;
import com.google.api.services.walletobjects.model.ImageUri;
import com.google.api.services.walletobjects.model.LoyaltyClass;
import com.loyalt.loyalt.exception.GoogleWalletException;
import com.loyalt.loyalt.exception.LoyaltyClassAlreadyExistsException;
import com.loyalt.loyalt.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class GoogleWalletClassService {
    private final Walletobjects walletobjects;
    //private String issuerId;
    private static final Logger logger = LoggerFactory.getLogger(PromotionService.class);

    public GoogleWalletClassService(Walletobjects walletobjects) {
        this.walletobjects = walletobjects;
        //this.issuerId = issuerId;
    }

    public String createLoyaltyClass(String googleClassId, String businessName, String programName, String logoURL) throws IOException {

        try {

            try {
                walletobjects.loyaltyclass().get(googleClassId).execute();
                throw new LoyaltyClassAlreadyExistsException("La loyalty class ya existe: " + googleClassId);

            } catch (GoogleJsonResponseException ex) {
                if (ex.getStatusCode() != 404) {
                    ex.printStackTrace();
                    return String.format("%s,$s", googleClassId);

                }
            }

            LoyaltyClass loyaltyClass = new LoyaltyClass()
                    .setId(googleClassId)
                    .setIssuerName(businessName)
                    .setProgramName(programName)
                    .setHexBackgroundColor("#7387E7")
                    .setReviewStatus("UNDER_REVIEW")
                    .setProgramLogo(new Image().setSourceUri(new ImageUri().setUri(logoURL)));

            LoyaltyClass googleResponse = walletobjects.loyaltyclass().
                    insert(loyaltyClass).
                    execute();

            logger.info("Google class added with id", googleResponse.getId());

            return googleResponse.getId();


        } catch (GoogleJsonResponseException e) {
            throw new GoogleWalletException("Error de Google Wallet: " + e.getDetails().getMessage(), e);

        } catch(Exception e){
            throw new GoogleWalletException("Error inesperado creando LoyaltyClass", e);

        }



    }
}