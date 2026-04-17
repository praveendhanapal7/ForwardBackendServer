package com.ForwadAgency.ForwardBackend.Service;

import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Repo.LeadsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LeadsService {

    @Autowired
    LeadsRepo leadsRepo;

    public List<Leads> getAllLeads()
    {
        return leadsRepo.findAll();
    }

    public Leads addLeads(Leads leads)
    {

        String a = leads.getPhoneNumber();

// remove spaces
        a = a.replace(" ", "");

// take last 10 digits (+1 for safety if needed)
        a = a.substring(a.length() - 10);

// add country code
        a = "+91" + a;

        leads.setPhoneNumber(a);

        System.out.println(a);

        return leadsRepo.save(leads);
    }

    public Leads updateLeadStatus(Integer id, String status)
    {
        Leads existingLead = leadsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));

        existingLead.setStatus(status);
        return leadsRepo.save(existingLead);
    }

    public List<Leads> listLeadsByClientName(String name)
    {
       return leadsRepo.findAllByClientName(name);
    }

}
