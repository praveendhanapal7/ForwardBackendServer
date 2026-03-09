package com.ForwadAgency.ForwardBackend.Service;

import com.ForwadAgency.ForwardBackend.Repo.AccessModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class AccessModelService {

    @Autowired
    AccessModelRepo accessModelRepo;

    public String getBrandName(String key)
    {
        System.out.println("This is "+key);

        System.out.println(accessModelRepo.getAccessModelBySecretKey(key));
        return accessModelRepo.getAccessModelBySecretKey(key).getBrandName();
    }

}
