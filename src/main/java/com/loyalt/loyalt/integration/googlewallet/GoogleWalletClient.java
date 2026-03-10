package com.loyalt.loyalt.integration.googlewallet;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.services.walletobjects.Walletobjects;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoogleWalletClient {
    private final Walletobjects walletobjects;

    public GoogleWalletClient(Walletobjects walletobjects){
        this.walletobjects = walletobjects;
    }

    public Walletobjects.Loyaltyclass getLoyaltyClass(){
        return walletobjects.loyaltyclass();
    }

    public Walletobjects.Loyaltyobject getLoyaltyObject() {
        return walletobjects.loyaltyobject();
    }


}
