package com.loyalt.loyalt.integration.googlewallet;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.walletobjects.model.*;
import com.loyalt.loyalt.dto.wallet.WalletObjectResponse;
import com.loyalt.loyalt.exception.BadRequestException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletAuthenticationException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletCommunicationException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class WalletObjectService {

    private final WalletClient googleWalletClient;
    private final WalletProperties walletProperties;
    private final GoogleWalletExceptionTranslator exceptionTranslator;
    private static final Logger logger = LoggerFactory.getLogger(WalletObjectService.class);

    public WalletObjectService(WalletClient googleWalletClient, WalletProperties walletProperties, GoogleWalletExceptionTranslator exceptionTranslator){
        this.googleWalletClient = googleWalletClient;
        this.walletProperties = walletProperties;
        this.exceptionTranslator = exceptionTranslator;
    }

    public String createGoogleWalletObject(UUID membershipId, String classId) {

        if (membershipId == null) {
            throw new BadRequestException("membershipId cannot be null");
        }

        if (classId == null || classId.isBlank()) {
            throw new BadRequestException("classId cannot be null");
        }

        String objectId = walletProperties.getIssuerId() + "." + membershipId;

        LoyaltyObject loyaltyObject = new LoyaltyObject()
                .setId(objectId)
                .setClassId(classId)
                .setState("ACTIVE")
                .setLoyaltyPoints(buildInitialPoints())
                .setBarcode(buildBarcode(membershipId));

        try {

            googleWalletClient
                    .getLoyaltyObject()
                    .insert(loyaltyObject)
                    .execute();

            return objectId;

        } catch (GoogleJsonResponseException e) {

            throw exceptionTranslator.translate(e);

        } catch (IOException e) {

            throw new GoogleWalletCommunicationException(
                    "Error communicating with Google Wallet",
                    e
            );
        }
    }

    public void updatePoints(String objectId, int points) {

        if (objectId == null || objectId.isBlank()) {
            throw new BadRequestException("objectId cannot be null");
        }

        if (points < 0) {
            throw new BadRequestException("Points cannot be negative");
        }

        LoyaltyPointsBalance balance = new LoyaltyPointsBalance();
        balance.setInt(points);

        LoyaltyPoints pointsObj = new LoyaltyPoints();
        pointsObj.setBalance(balance);

        LoyaltyObject object = new LoyaltyObject();
        object.setLoyaltyPoints(pointsObj);

        try {

            googleWalletClient
                    .getLoyaltyObject()
                    .patch(objectId, object)
                    .execute();

        } catch (GoogleJsonResponseException e) {

            throw exceptionTranslator.translate(e);

        } catch (IOException e) {

            throw new GoogleWalletCommunicationException(
                    "Error communicating with Google Wallet",
                    e
            );
        }
    }

    public List<WalletObjectResponse> getObjectsByClass(String classId) {

        if (classId == null || classId.isBlank()) {
            throw new BadRequestException("classId cannot be null");
        }

        try {

            LoyaltyObjectListResponse response = googleWalletClient
                    .getLoyaltyObject()
                    .list()
                    .setClassId(classId)
                    .execute();

            if (response.getResources() == null) {
                return List.of();
            }

            return response.getResources()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

        } catch (GoogleJsonResponseException e) {

            throw exceptionTranslator.translate(e);

        } catch (IOException e) {

            throw new GoogleWalletCommunicationException(
                    "Error communicating with Google Wallet",
                    e
            );
        }
    }

    private LoyaltyPoints buildInitialPoints() {

        LoyaltyPointsBalance balance = new LoyaltyPointsBalance();
        balance.setInt(0);

        LoyaltyPoints points = new LoyaltyPoints();
        points.setBalance(balance);

        return points;
    }

    private Barcode buildBarcode(UUID membershipId) {

        Barcode barcode = new Barcode();
        barcode.setType("QR_CODE");
        //barcode.setValue(membershipId.toString());
        barcode.setValue("Muestra este QR en caja");

        return barcode;
    }

    private WalletObjectResponse mapToResponse(LoyaltyObject obj) {

        int points = 0;

        if (obj.getLoyaltyPoints() != null &&
                obj.getLoyaltyPoints().getBalance() != null) {

            points = obj.getLoyaltyPoints().getBalance().getInt();
        }

        return new WalletObjectResponse(
                obj.getId(),
                obj.getAccountId(),
                obj.getAccountName(),
                obj.getState(),
                points
        );
    }

    public void deactivateObject(String objectId) {

        if (objectId == null || objectId.isBlank()) {
            throw new BadRequestException("objectId cannot be null");
        }

        LoyaltyObject object = new LoyaltyObject();
        object.setState("INACTIVE");

        try {

            googleWalletClient
                    .getLoyaltyObject()
                    .patch(objectId, object)
                    .execute();

        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                logger.warn("Object already inactive: {}", objectId);
                return;
            }

            throw exceptionTranslator.translate(e);

        } catch (IOException e) {

            throw new GoogleWalletCommunicationException(
                    "Error communicating with Google Wallet",
                    e
            );
        }
    }

    public void deactivateObjectsByClass(String objectId) {

        List<WalletObjectResponse> objects = getObjectsByClass(objectId);

        for (WalletObjectResponse obj : objects) {
            deactivateObject(obj.objectId());
        }
    }



}
