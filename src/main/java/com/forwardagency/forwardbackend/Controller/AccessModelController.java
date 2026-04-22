package com.forwardagency.forwardbackend.Controller;

import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.Service.AccessModelService;
import com.forwardagency.forwardbackend.web.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccessModelController {

    @Autowired
    private AccessModelService accessModelService;

    @GetMapping("/get/all/brands")
    public ResponseEntity<List<String>> listBrands(HttpServletRequest request) {
        return brandsForCaller(CurrentUser.require(request));
    }

    @PostMapping("/get/all/brands")
    public ResponseEntity<List<String>> listBrandsLegacy(HttpServletRequest request) {
        return brandsForCaller(CurrentUser.require(request));
    }

    private ResponseEntity<List<String>> brandsForCaller(Users caller) {
        if (!CurrentUser.isAgency(caller)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(accessModelService.getAllBrands());
    }
}
