package com.loyalt.loyalt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="google.wallet")
public class GoogleWalletProperties {
    private String issuerId;
    private String qrBaseUrl;
    private String qrBaseUrlPath;
    private String qrBaseUrlQueryParam;

    public String getIssuerId(){
        return issuerId;
    }

    public void setIssuerId(String issuerId){
        this.issuerId = issuerId;
    }

    public String getQrBaseUrl() {
        return qrBaseUrl;
    }

    public void setQrBaseUrl(String qrBaseUrl) {
        this.qrBaseUrl = qrBaseUrl;
    }

    public String getQrBaseUrlPath() {
        return qrBaseUrlPath;
    }

    public void setQrBaseUrlPath(String qrBaseUrlPath) {
        this.qrBaseUrlPath = qrBaseUrlPath;
    }

    public String getQrBaseUrlQueryParam() {
        return qrBaseUrlQueryParam;
    }

    public void setQrBaseUrlQueryParam(String qrBaseUrlQueryParam) {
        this.qrBaseUrlQueryParam = qrBaseUrlQueryParam;
    }
}
