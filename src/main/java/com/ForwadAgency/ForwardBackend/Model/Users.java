package com.ForwadAgency.ForwardBackend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.security.PrivateKey;

@Entity
@Data
public class Users {

    private String name;
    @Id
    private String email;
    private String phoneNumber;
    private String location;
    private String accountType;
    private String  brandName;
    private String  password;
    private String  secretKey;
}
