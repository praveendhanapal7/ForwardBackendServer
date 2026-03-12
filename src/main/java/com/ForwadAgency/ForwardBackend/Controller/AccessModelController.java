package com.ForwadAgency.ForwardBackend.Controller;


import com.ForwadAgency.ForwardBackend.Model.AccessModel;
import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Repo.AccessModelRepo;
import com.ForwadAgency.ForwardBackend.Service.AccessModelService;
import com.ForwadAgency.ForwardBackend.Service.LeadsService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccessModelController
{

    @Autowired
    AccessModelService accessModelService;

    @Autowired
    AccessModelRepo accessModelRepo;

    @Autowired
    LeadsService leadsService;



    @PostMapping("/get/leads/brandname")
    public List<Leads> getAllLeads(@RequestBody AccessModel accessModel)
    {

        System.out.println("Hello "+accessModel);

        String brandName = accessModelService.getBrandName(accessModel.getSecretKey());

       if(brandName==null) {
           return null;
       }
        return leadsService.listLeadsByClientName(brandName);
    }


    @PostMapping("/get/brandname")
    public String getAllLeadsb(@RequestBody AccessModel accessModel)
    {
        return accessModelService.getBrandName(accessModel.getSecretKey());
    }

    @PostMapping("/get/all/brands")
    public List<String> getAllUsers(@RequestBody AccessModel access)
    {
        System.out.println("This is the object from client : "+access.getSecretKey());

        if(access.getSecretKey().equals("forward@2025"))
        {
            return accessModelService.getAllBrands("forward@2025");
        }
        else
            return null;
    }

}
