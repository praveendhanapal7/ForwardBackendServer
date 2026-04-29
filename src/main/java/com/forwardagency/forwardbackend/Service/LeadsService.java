package com.forwardagency.forwardbackend.Service;

import com.forwardagency.forwardbackend.Model.Leads;
import com.forwardagency.forwardbackend.Repo.LeadsRepo;
import com.forwardagency.forwardbackend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class LeadsService {

    private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");
    private static final DateTimeFormatter LOG_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy, h:mm a", Locale.ENGLISH);

    @Autowired
    private LeadsRepo leadsRepo;

    public List<Leads> getAllLeads() {
        return leadsRepo.findAll();
    }

    public Leads addLeads(Leads leads) {
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

    public Leads updateLeadStatus(Integer id, String status, String changedBy) {
        Leads existingLead = leadsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        existingLead.setStatus(status);
        existingLead.setLog(appendAuditLog(existingLead.getLog(), "Status changed to " + status, changedBy));
        return leadsRepo.save(existingLead);
    }

    public List<Leads> listLeadsByClientName(String name) {
        return leadsRepo.findAllByClientName(name);
    }

    public Leads addNotes(Leads leads, String changedBy) {
        Leads existingLead = leadsRepo.findById(leads.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Lead not found with id: " + leads.getId()));

        String previousNotes = existingLead.getNotes();
        String updatedNotes = leads.getNotes();
        existingLead.setNotes(updatedNotes);

        String action = (previousNotes == null || previousNotes.isBlank())
                ? "Notes added"
                : "Notes updated";
        existingLead.setLog(appendAuditLog(existingLead.getLog(), action, changedBy));

        return leadsRepo.save(existingLead);
    }

    private String appendAuditLog(String currentLog, String action, String changedBy) {
        String actor = changedBy == null || changedBy.isBlank() ? "Unknown user" : changedBy;
        String time = LocalDateTime.now(INDIA_ZONE)
                .format(LOG_TIME_FORMATTER)
                .toLowerCase(Locale.ENGLISH);
        String entry = "[" + time + "] " + action + " by " + actor;

        if (currentLog == null || currentLog.isBlank()) {
            return entry;
        }

        return currentLog + System.lineSeparator() + entry;
    }
}
