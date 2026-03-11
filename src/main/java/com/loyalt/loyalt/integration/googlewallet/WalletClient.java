package com.loyalt.loyalt.integration.googlewallet;


import com.google.api.services.walletobjects.Walletobjects;
import org.springframework.stereotype.Component;

@Component
public class WalletClient {
    private final Walletobjects walletobjects;

    public WalletClient(Walletobjects walletobjects){
        this.walletobjects = walletobjects;
    }

    public Walletobjects.Loyaltyclass getLoyaltyClass(){
        return walletobjects.loyaltyclass();
    }

    public Walletobjects.Loyaltyobject getLoyaltyObject() {
        return walletobjects.loyaltyobject();
    }


}
