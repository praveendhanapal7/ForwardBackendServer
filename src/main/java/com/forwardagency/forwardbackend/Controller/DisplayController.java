package com.forwardagency.forwardbackend.Controller;

import com.forwardagency.forwardbackend.Model.Leads;
import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.Service.LeadsService;
import com.forwardagency.forwardbackend.web.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class DisplayController {

    @Autowired
    private LeadsService leadsService;

    @GetMapping("/get/leads/all")
    public List<Leads> getAllLeads(HttpServletRequest request) {
        return leadsForCaller(CurrentUser.require(request));
    }

    @PostMapping("/get/leads/all")
    public List<Leads> getAllLeadsLegacy(HttpServletRequest request) {
        return leadsForCaller(CurrentUser.require(request));
    }

    private List<Leads> leadsForCaller(Users caller) {
        if (CurrentUser.isAgency(caller)) {
            return leadsService.getAllLeads();
        }
        if (caller.getBrandName() == null || caller.getBrandName().isBlank()) {
            return Collections.emptyList();
        }
        return leadsService.listLeadsByClientName(caller.getBrandName());
    }
}
