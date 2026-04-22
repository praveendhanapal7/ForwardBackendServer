package com.forwardagency.forwardbackend.Controller;

import com.forwardagency.forwardbackend.Model.Leads;
import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.Service.AuthTokenService;
import com.forwardagency.forwardbackend.Service.LeadsService;
import com.forwardagency.forwardbackend.Service.UserService;
import com.forwardagency.forwardbackend.exception.InvalidCredentialsException;
import com.forwardagency.forwardbackend.web.CurrentUser;
import com.forwardagency.forwardbackend.web.dto.AuthResponse;
import com.forwardagency.forwardbackend.web.dto.LeadStatusRequest;
import com.forwardagency.forwardbackend.web.dto.LoginRequest;
import com.forwardagency.forwardbackend.web.dto.UserView;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffController {

    private static final Logger log = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private LeadsService leadsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenService authTokenService;

    @GetMapping("/status")
    public String sayHello() {
        return "Hii , I'm Working!";
    }

    @PostMapping("/add/leads")
    public Leads addLeads(@RequestBody Leads leads, HttpServletRequest request) {
        Users caller = CurrentUser.require(request);

        if (!CurrentUser.isAgency(caller)) {
            leads.setClientName(caller.getBrandName());
        } else if (leads.getClientName() == null || leads.getClientName().isBlank()) {
            throw new IllegalArgumentException("Client name is required");
        }

        if (leads.getAddedBy() == null || leads.getAddedBy().isBlank()) {
            leads.setAddedBy(
                    CurrentUser.isAgency(caller)
                            ? caller.getName() + " (Agency Member)"
                            : caller.getName());
        }

        return leadsService.addLeads(leads);
    }

    @PostMapping("/add/notes")
    public Leads addingNotes(@RequestBody Leads leads, HttpServletRequest request) {
        CurrentUser.require(request);
        return leadsService.addNotes(leads);
    }

    @PutMapping("/leads/{id}/status")
    public Leads updateLeadStatus(@PathVariable Integer id,
                                  @RequestBody LeadStatusRequest body,
                                  HttpServletRequest request) {
        CurrentUser.require(request);
        if (body == null || body.status() == null || body.status().isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }
        return leadsService.updateLeadStatus(id, body.status());
    }

    @PostMapping("/add/user")
    public ResponseEntity<?> addUsers(@RequestBody Users user) {
        try {
            Users saved = userService.addUser(user);
            String token = authTokenService.issueToken(saved);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(token, UserView.from(saved)));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        } catch (Exception exception) {
            log.error("Failed to add user", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to create account right now");
        }
    }

    @PostMapping("/user/auth/login")
    public AuthResponse checkUser(@RequestBody LoginRequest body) {
        if (body == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        log.debug("Login attempt for email: {}", body.email());

        Users candidate = new Users();
        candidate.setEmail(body.email());
        candidate.setPassword(body.password());

        Users authenticated = userService.authUser(candidate);
        String token = authTokenService.issueToken(authenticated);
        return new AuthResponse(token, UserView.from(authenticated));
    }

    @PostMapping("/user/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = CurrentUser.token(request);
        if (token != null) {
            authTokenService.revoke(token);
        }
        return ResponseEntity.noContent().build();
    }
}
