package com.ForwadAgency.ForwardBackend.Controller;

import com.ForwadAgency.ForwardBackend.Model.AccessModel;
import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Model.Users;
import com.ForwadAgency.ForwardBackend.Service.LeadsService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DisplayController
{
    @Autowired
    LeadsService leadsService;


    @PostMapping("/get/leads/all")
    public List<Leads> getAllLeadss(@RequestBody Users user)
    {
         if(user.getSecretKey().equals("forward@2025"))
            return leadsService.getAllLeads();
         else {
             System.out.println("client name : "+user);
             return leadsService.listLeadsByClientName(user.getBrandName());
         }
    }

}
