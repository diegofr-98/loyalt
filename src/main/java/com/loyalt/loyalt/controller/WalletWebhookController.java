package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.wallet.SyncPointsRequest;
import com.loyalt.loyalt.exception.UnauthorizedException;
import com.loyalt.loyalt.integration.googlewallet.WalletObjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/webhooks/wallet")
public class WalletWebhookController {

    private final WalletObjectService walletObjectService;
    private final String webhookSecret;
    private static final Logger logger = LoggerFactory.getLogger(WalletWebhookController.class);
    public WalletWebhookController(WalletObjectService walletObjectService,  @Value("${webhook.secret}") String webhookSecret){
        this.walletObjectService = walletObjectService;
        this.webhookSecret = webhookSecret;

    }

    @PostMapping("/sync-points")
    public ResponseEntity<Void> syncPoints(
            @RequestHeader(value = "X-WEBHOOK-SECRET", required = false) String secret,
            @RequestBody Map<String, Object> payload) {


        logger.info("sync-points controller");
        if (!Objects.equals(webhookSecret, secret)) {
            throw new UnauthorizedException("Invalid webhook secret");
        }

        String objectId = (String) payload.get("objectId");
        Integer points = (Integer) payload.get("pointsBalance");

        walletObjectService.syncPointsFromWebhook(objectId, points);

        return ResponseEntity.ok().build();
    }
}
