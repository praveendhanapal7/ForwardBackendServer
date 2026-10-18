package dev.forward.Forward.Controller;

import dev.forward.Forward.Model.Enquiry;
import dev.forward.Forward.Model.User;
import dev.forward.Forward.Service.EnquiryService;
import dev.forward.Forward.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {



    @Autowired
    private UserService userservice;

    @Autowired
    EnquiryService EnquirySerive;


    @PostMapping("/create/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userservice.createUser(user) , HttpStatus.CREATED);
    }

    @PostMapping("/add/enquiry")
    public ResponseEntity<Enquiry> adfedEnquiry(@RequestBody Enquiry enquiry) {
        return new ResponseEntity<>(EnquirySerive.addEnquiry(enquiry) , HttpStatus.CREATED);
    }

    @GetMapping("enquiries/all")
    public List<Enquiry> getEnquiries()
    {
        return EnquirySerive.listAllEnquiries();
    }

    @GetMapping("enquiries/all/{d}")
    public List<Enquiry> getEnquiries(@PathVariable("d") String d)
    {
        return EnquirySerive.getEnquiriesByClient(d);
    }

}
