package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.dto.wallet.WalletObjectResponse;
import com.loyalt.loyalt.integration.googlewallet.WalletObjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletObjectService walletObjectService;
    public WalletController(WalletObjectService walletObjectService){
        this.walletObjectService = walletObjectService;
    }

    @GetMapping("{classId}/objects")
    public ResponseEntity<List<WalletObjectResponse>> getObjectsByClass(@PathVariable String classId) throws IOException {
        List<WalletObjectResponse> response = walletObjectService.getObjectsByClass(classId);
        return ResponseEntity.ok(response);


    }



}
