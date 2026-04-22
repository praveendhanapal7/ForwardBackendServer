package com.forwardagency.forwardbackend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AccessModel {

    @Id
    private String secretKey;
    private String brandName;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
