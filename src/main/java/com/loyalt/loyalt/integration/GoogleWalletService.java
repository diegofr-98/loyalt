package com.loyalt.loyalt.integration;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.walletobjects.Walletobjects;
import com.google.api.services.walletobjects.model.Image;
import com.google.api.services.walletobjects.model.ImageUri;
import com.google.api.services.walletobjects.model.LoyaltyClass;
import com.loyalt.loyalt.exception.GoogleWalletException;
import com.loyalt.loyalt.exception.LoyaltyClassAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class GoogleWalletService {
    private final Walletobjects walletobjects;
    //private String issuerId;

    public GoogleWalletService(Walletobjects walletobjects) {
        this.walletobjects = walletobjects;
        //this.issuerId = issuerId;
    }

    public String createLoyaltyClass(String googleClassId, String businessName, String programName) throws IOException {
        /*TODO: añade el campo de logo a la base de datos, añade logs de que la google class id fue generada correctamente, como generar el qr, revisar que pasa si mi correo no es el dado de alta*/
        final String DEFAULT_LOGO = "https://rifsoxpgwhcnyklspgsc.supabase.co/storage/v1/object/public/business-logos/test_logo.png";
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

            //https://rifsoxpgwhcnyklspgsc.supabase.co/storage/v1/object/public/business-logos/test_logo.png

            LoyaltyClass loyaltyClass = new LoyaltyClass()
                    .setId(googleClassId)
                    .setIssuerName(businessName)
                    .setProgramName(programName)
                    .setHexBackgroundColor("#7387E7")
                    .setReviewStatus("UNDER_REVIEW")
                    .setProgramLogo(new Image().setSourceUri(new ImageUri().setUri(DEFAULT_LOGO)));

            LoyaltyClass googleResponse = walletobjects.loyaltyclass().
                    insert(loyaltyClass).
                    execute();

            return googleResponse.getId();


        } catch (GoogleJsonResponseException e) {
            throw new GoogleWalletException("Error de Google Wallet: " + e.getDetails().getMessage(), e);

        } catch(Exception e){
            throw new GoogleWalletException("Error inesperado creando LoyaltyClass", e);

        }



    }
}