package com.ForwadAgency.ForwardBackend.Service;

import com.ForwadAgency.ForwardBackend.Model.AccessModel;
import com.ForwadAgency.ForwardBackend.Model.Users;
import com.ForwadAgency.ForwardBackend.Repo.AccessModelRepo;
import com.ForwadAgency.ForwardBackend.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    AccessModelService accessModelService;

    @Autowired
    AccessModelRepo accessModelRepo;

    @Transactional
    public Users addUser(Users user) {

        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (user.getSecretKey() == null || user.getSecretKey().isBlank()) {
            throw new IllegalArgumentException("Access code is required");
        }

        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("User already exists");
        }

        String brandName = user.getBrandName();
        AccessModel existingAccessModel = accessModelRepo.getAccessModelBySecretKey(user.getSecretKey());

        if (brandName != null && !brandName.isBlank()) {
            if (existingAccessModel == null) {
                AccessModel newAccessModel = new AccessModel();
                newAccessModel.setSecretKey(user.getSecretKey());
                newAccessModel.setBrandName(brandName);
                accessModelRepo.save(newAccessModel);
            } else if (!brandName.equals(existingAccessModel.getBrandName())) {
                throw new IllegalStateException("Access code already belongs to another brand");
            }
        } else {
            brandName = accessModelService.getBrandName(user.getSecretKey());
        }

        if ((user.getAccountType() == null || user.getAccountType().isBlank())
                && brandName != null && !brandName.isBlank()) {
            user.setAccountType("client");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getEmail());
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            user.setPhoneNumber("NA");
        }

        if ((user.getAccountType() == null || user.getAccountType().isBlank())
                || user.getAccountType().equalsIgnoreCase("staff")
                || user.getAccountType().equalsIgnoreCase("Agency Staff")) {
            if (brandName == null || brandName.isBlank()) {
                throw new IllegalArgumentException("Access code is invalid");
            }
        }

        user.setBrandName(brandName);
        try {
            return userRepo.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException("Unable to create user with the provided details");
        }
    }




    public Users authUser(Users user) {
        Users dbUser = userRepo.findByEmail(user.getEmail());

        if (dbUser == null) {
            throw new RuntimeException("User not found");
        }

        if (dbUser.getPassword().equals(user.getPassword())) {
            return dbUser;
        } else {
            System.out.println(user.getPassword()+"   "+dbUser);
            throw new RuntimeException("Invalid password");
        }


    }
    }
