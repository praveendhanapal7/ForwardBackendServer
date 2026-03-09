package com.ForwadAgency.ForwardBackend.Controller;

import com.ForwadAgency.ForwardBackend.Model.AccessModel;
import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Service.LeadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DisplayController
{
    @Autowired
    LeadsService leadsService;


    @PostMapping("/get/leads/all")
    public List<Leads> getAllLeadss(@RequestBody AccessModel key)
    {
        if(key.getSecretKey().equals("forward@2025")) {
            return leadsService.getAllLeads();
        }        else
            return null;
    }

}
