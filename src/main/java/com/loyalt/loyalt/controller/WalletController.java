package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.wallet.WalletObjectResponse;
import com.loyalt.loyalt.integration.googlewallet.WalletClassService;
import com.loyalt.loyalt.integration.googlewallet.WalletObjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletObjectService walletObjectService;
    private final WalletClassService walletClassService;
    public WalletController(WalletObjectService walletObjectService, WalletClassService walletClassService){
        this.walletObjectService = walletObjectService;
        this.walletClassService = walletClassService;
    }

    @GetMapping("{classId}/objects")
    public ResponseEntity<List<WalletObjectResponse>> getObjectsByClass(@PathVariable String classId) throws IOException {
        List<WalletObjectResponse> response = walletObjectService.getObjectsByClass(classId);
        return ResponseEntity.ok(response);


    }

    @PatchMapping("/objects/{objectId}/deactivate")
    public ResponseEntity<Void> deactivateClass(@PathVariable String objectId) {

        walletObjectService.deactivateObjectsByClass(objectId);
        return ResponseEntity.noContent().build();
    }



}
