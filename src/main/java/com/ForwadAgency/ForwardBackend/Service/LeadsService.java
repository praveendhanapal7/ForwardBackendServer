package com.ForwadAgency.ForwardBackend.Service;

import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Repo.LeadsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LeadsService {

    @Autowired
    LeadsRepo leadsRepo;
    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    public List<Leads> getAllLeads()
    {
        return leadsRepo.findAll();
    }

    public Leads addLeads(Leads leads)
    {
        leadsRepo.save(leads);
        return leads;
    }

    public List<Leads> listLeadsByClientName(String name)
    {
       return leadsRepo.findAllByClientName(name);
    }

}

