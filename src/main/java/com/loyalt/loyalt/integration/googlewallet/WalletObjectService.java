package com.loyalt.loyalt.integration.googlewallet;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.walletobjects.model.*;
import com.loyalt.loyalt.config.GoogleWalletProperties;
import com.loyalt.loyalt.dto.wallet.WalletObjectResponse;
import com.loyalt.loyalt.exception.BadRequestException;
import com.loyalt.loyalt.exception.NotFoundException;
import com.loyalt.loyalt.exception.wallet.GoogleWalletCommunicationException;
import com.loyalt.loyalt.model.entity.CustomerBusiness;
import com.loyalt.loyalt.repository.CustomerBusinessRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class WalletObjectService {

    private final WalletClient googleWalletClient;
    private final GoogleWalletProperties googleWalletProperties;
    private final GoogleWalletExceptionTranslator exceptionTranslator;
    private final CustomerBusinessRepository customerBusinessRepository;
    private static final Logger logger = LoggerFactory.getLogger(WalletObjectService.class);

    public WalletObjectService(WalletClient googleWalletClient,
                               GoogleWalletProperties googleWalletProperties,
                               GoogleWalletExceptionTranslator exceptionTranslator,
                               CustomerBusinessRepository customerBusinessRepository){
        this.googleWalletClient = googleWalletClient;
        this.googleWalletProperties = googleWalletProperties;
        this.exceptionTranslator = exceptionTranslator;
        this.customerBusinessRepository = customerBusinessRepository;
    }

    public String createGoogleWalletObject(UUID customerId, String classId) {

        if (customerId == null) {
            throw new BadRequestException("membershipId cannot be null");
        }

        if (classId == null || classId.isBlank()) {
            throw new BadRequestException("classId cannot be null");
        }

        String objectId = googleWalletProperties.getIssuerId() + "." + customerId;

        LoyaltyObject loyaltyObject = new LoyaltyObject()
                .setId(objectId)
                .setClassId(classId)
                .setState("ACTIVE")
                .setLoyaltyPoints(buildInitialPoints())
                .setBarcode(buildBarcode(customerId));

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

    @Transactional
    public void syncPointsFromWebhook(String objectId, int pointsBalance) {

        CustomerBusiness cb = customerBusinessRepository
                .findByGoogleObjectId(objectId)
                .orElseThrow(() ->
                        new NotFoundException("Customer business not found for objectId: " + objectId));

        /*if (cb.getPointsBalance() == pointsBalance) {
            logger.info("Points registered are up to date");
            return;
        }*/

        updatePoints(objectId, pointsBalance);
    }

    private void updatePoints(String objectId, int points) {
        logger.info(
                "Updating Google Wallet points: objectId={}, points={}",
                objectId,
                points
        );

        validateUpdatePointsRequest(objectId, points);

        LoyaltyObject patchObject = buildPointsPatch(points);

        try {

            googleWalletClient
                    .getLoyaltyObject()
                    .patch(objectId, patchObject)
                    .execute();
            logger.info("Google Wallet points updated successfully: objectId={}", objectId);

        } catch (GoogleJsonResponseException e) {

            throw exceptionTranslator.translate(e);

        } catch (IOException e) {

            throw new GoogleWalletCommunicationException(
                    "Error communicating with Google Wallet while updating points",
                    e
            );
        }
    }

    private void validateUpdatePointsRequest(String objectId, int points) {

        if (objectId == null || objectId.isBlank()) {
            throw new BadRequestException("objectId cannot be null or blank");
        }

        if (points < 0) {
            throw new BadRequestException("Points cannot be negative");
        }
    }

    private LoyaltyObject buildPointsPatch(int points) {

        LoyaltyPointsBalance balance = new LoyaltyPointsBalance();
        balance.setInt(points);

        LoyaltyPoints loyaltyPoints = new LoyaltyPoints();
        loyaltyPoints.setBalance(balance);

        LoyaltyObject loyaltyObject = new LoyaltyObject();
        loyaltyObject.setLoyaltyPoints(loyaltyPoints);

        return loyaltyObject;
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
        barcode.setValue(buildQrUrl(membershipId));

        return barcode;
    }

    private String buildQrUrl(UUID customerBusinessId) {

        String qrBaseUrl = googleWalletProperties.getQrBaseUrl();
        String qrBaseUrlPath = googleWalletProperties.getQrBaseUrlPath();
        String qrBaseUrlQueryParam = googleWalletProperties.getQrBaseUrlQueryParam();
        return UriComponentsBuilder.
                fromHttpUrl(qrBaseUrl)
                .path(qrBaseUrlPath)
                .queryParam(qrBaseUrlQueryParam, customerBusinessId)
                .toUriString();
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
