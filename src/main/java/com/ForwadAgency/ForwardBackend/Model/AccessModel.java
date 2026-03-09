package com.ForwadAgency.ForwardBackend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class AccessModel {

    @Id
    private String secretKey;
    private String brandName;
}
