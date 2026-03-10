package com.loyalt.loyalt.integration.googlewallet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="google.wallet")
public class GoogleWalletProperties {
    public String issuerId;

    public String getIssuerId(){
        return issuerId;
    }

    public void setIssuerId(String issuerId){
        this.issuerId = issuerId;
    }
}
