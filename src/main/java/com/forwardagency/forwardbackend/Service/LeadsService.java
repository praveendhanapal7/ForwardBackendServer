package com.forwardagency.forwardbackend.Service;

import com.forwardagency.forwardbackend.Model.Leads;
import com.forwardagency.forwardbackend.Repo.LeadsRepo;
import com.forwardagency.forwardbackend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadsService {

    @Autowired
    private LeadsRepo leadsRepo;

    public List<Leads> getAllLeads()
    {
        return leadsRepo.findAll();
    }

    public Leads addLeads(Leads leads)
    {

        String a = leads.getPhoneNumber();
        if (a == null || a.isBlank()) {
            return leadsRepo.save(leads);
        }

        a = a.replace(" ", "");
        if (a.length() < 10) {
            return leadsRepo.save(leads);
        }

        a = "+91" + a.substring(a.length() - 10);
        leads.setPhoneNumber(a);

        return leadsRepo.save(leads);
    }

    public Leads updateLeadStatus(Integer id, String status) {
        Leads existingLead = leadsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        existingLead.setStatus(status);
        return leadsRepo.save(existingLead);
    }

    public List<Leads> listLeadsByClientName(String name)
    {
       return leadsRepo.findAllByClientName(name);
    }

    public Leads addNotes(Leads leads) {
        Leads existingLead = leadsRepo.findById(leads.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Lead not found with id: " + leads.getId()));

        existingLead.setNotes(leads.getNotes());
        return leadsRepo.save(existingLead);
    }
}
