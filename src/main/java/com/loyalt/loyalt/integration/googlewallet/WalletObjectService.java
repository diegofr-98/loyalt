package com.loyalt.loyalt.integration.googlewallet;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.walletobjects.model.*;
import com.loyalt.loyalt.dto.wallet.WalletObjectResponse;
import com.loyalt.loyalt.exception.wallet.GoogleWalletException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class WalletObjectService {

    private final WalletClient googleWalletClient;
    private final WalletProperties walletProperties;

    public WalletObjectService(WalletClient googleWalletClient, WalletProperties walletProperties){
        this.googleWalletClient = googleWalletClient;
        this.walletProperties = walletProperties;
    }

    public String createGoogleWalletObject(UUID membershipId, String classId) throws IOException {
        if(membershipId == null){
            throw  new IllegalArgumentException("membershipId cannot be null");
        }

        if(classId == null || classId.isBlank()){
            throw new IllegalArgumentException("classId cannot be null");
        }

        String objectId = walletProperties.getIssuerId() + "." + membershipId;

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

    public List<WalletObjectResponse> getObjectsByClass(String classId) throws IOException {
        LoyaltyObjectListResponse response = null;
        try{
        response = googleWalletClient
                .getLoyaltyObject()
                .list()
                .setClassId(classId)
                .execute();

        if (response.getResources() == null) {
            return List.of();
        }


        }catch(GoogleJsonResponseException e){
            if(e.getStatusCode() == 404){
                throw new GoogleWalletException("Class not found: " + classId);
            }

        }


        return response.getResources()
                .stream()
                .map(obj -> new WalletObjectResponse(
                        obj.getId(),
                        obj.getAccountId(),
                        obj.getAccountName(),
                        obj.getLoyaltyPoints() != null ? obj.getLoyaltyPoints().getBalance().getInt() : 0))
                .toList();
    }


}
