package com.ForwadAgency.ForwardBackend.Controller;

import com.ForwadAgency.ForwardBackend.Model.Leads;
import com.ForwadAgency.ForwardBackend.Model.Users;
import com.ForwadAgency.ForwardBackend.Service.LeadsService;
import com.ForwadAgency.ForwardBackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/add/leads")
    public Leads addLeads(@RequestBody Leads leads)
    {
        return leadsService.addLeads(leads);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/add/user")
    public Users addUsers(@RequestBody Users user)
    {
        System.out.println("User : "+user);
        return userService.addUser(user);
    }


    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/user/auth/login")
    public Users checkUser(@RequestBody Users user)
    {
        System.out.println("this is name : "+user.getName());

        return userService.authUser(user);
    }

}
