package com.ForwadAgency.ForwardBackend.Controller;

import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Model.Users;
import com.ForwadAgency.ForwardBackend.Service.LeadsService;
import com.ForwadAgency.ForwardBackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StaffController {

    @Autowired
    LeadsService leadsService;

    @Autowired
    UserService userService;

    @GetMapping("/status")
    public String sayHello()
    {
        return "Hii , I'm Working!";
    }

    @CrossOrigin(origins = {"http://localhost:5173", "https://forwardagency.in", "https://www.forwardagency.in"})
    @PostMapping("/add/leads")
    public Leads addLeads(@RequestBody Leads leads)
    {
        return leadsService.addLeads(leads);
    }

    @CrossOrigin(origins = {"http://localhost:5173", "https://forwardagency.in", "https://www.forwardagency.in"})
    @PutMapping("/leads/{id}/status")
    public Leads updateLeadStatus(@PathVariable Integer id, @RequestBody Leads leads)
    {
        return leadsService.updateLeadStatus(id, leads.getStatus());
    }

    @CrossOrigin(origins = {"http://localhost:5173", "https://forwardagency.in", "https://www.forwardagency.in"})
    @PostMapping("/add/user")
    public ResponseEntity<?> addUsers(@RequestBody Users user)
    {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to create account right now");
        }
    }


    @CrossOrigin(origins = {"http://localhost:5173", "https://forwardagency.in", "https://www.forwardagency.in"})
    @PostMapping("/user/auth/login")
    public Users checkUser(@RequestBody Users user)
    {
        System.out.println("this is name : "+user.getName());

        return userService.authUser(user);
    }

}
