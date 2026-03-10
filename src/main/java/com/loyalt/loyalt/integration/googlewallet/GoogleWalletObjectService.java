package com.loyalt.loyalt.integration.googlewallet;

import com.google.api.services.walletobjects.model.Barcode;
import com.google.api.services.walletobjects.model.LoyaltyObject;
import com.google.api.services.walletobjects.model.LoyaltyPoints;
import com.google.api.services.walletobjects.model.LoyaltyPointsBalance;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class GoogleWalletObjectService {

    private final GoogleWalletClient googleWalletClient;
    private final GoogleWalletProperties googleWalletProperties;

    public GoogleWalletObjectService(GoogleWalletClient googleWalletClient, GoogleWalletProperties googleWalletProperties){
        this.googleWalletClient = googleWalletClient;
        this.googleWalletProperties = googleWalletProperties;
    }

    public String createGoogleWalletObject(UUID membershipId, String classId) throws IOException {
        if(membershipId == null){
            throw  new IllegalArgumentException("membershipId cannot be null");
        }

        if(classId == null || classId.isBlank()){
            throw new IllegalArgumentException("classId cannot be null");
        }

        String objectId = googleWalletProperties.getIssuerId() + "." + membershipId;

        LoyaltyObject loyaltyObject = new LoyaltyObject();
        loyaltyObject.setId(objectId);
        loyaltyObject.setClassId(classId);
        loyaltyObject.setState("ACTIVE");

        //Setting initial points
        LoyaltyPointsBalance balance = new LoyaltyPointsBalance();
        balance.setInt(0);
        LoyaltyPoints points = new LoyaltyPoints();
        points.setBalance(balance);
        loyaltyObject.setLoyaltyPoints(points);
        //QR
        Barcode barcode = new Barcode();
        barcode.setType("QR_CODE");
        barcode.setValue(membershipId.toString());
        loyaltyObject.setBarcode(barcode);

        googleWalletClient
                .getLoyaltyObject()
                .insert(loyaltyObject)
                .execute();

        return objectId;

    }

    public void updatePoints(String objectId, int points) throws IOException {

        if (objectId == null || objectId.isBlank()) {
            throw new IllegalArgumentException("objectId cannot be null");
        }

        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }

        LoyaltyObject object = new LoyaltyObject();

        LoyaltyPoints pointsObj = new LoyaltyPoints();
        LoyaltyPointsBalance balance = new LoyaltyPointsBalance();

        balance.setInt(points);

        pointsObj.setBalance(balance);

        object.setLoyaltyPoints(pointsObj);

        googleWalletClient
                .getLoyaltyObject()
                .patch(objectId, object)
                .execute();
    }

}
