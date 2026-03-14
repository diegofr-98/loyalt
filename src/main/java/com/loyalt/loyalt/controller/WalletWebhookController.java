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
    public ResponseEntity<Void> syncPoints(@RequestHeader("X-WEBHOOK-SECRET") String secret,
            @Valid @RequestBody SyncPointsRequest request) {

        if (!webhookSecret.equals(secret)) {
            throw new UnauthorizedException("Invalid webhook secret");
        }

        walletObjectService.syncPointsFromWebhook(
                request.objectId(),
                request.pointsBalance()
        );

        return ResponseEntity.ok().build();
    }
}
