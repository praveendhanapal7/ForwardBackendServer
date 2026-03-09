package com.ForwadAgency.ForwardBackend.Service;

import com.ForwadAgency.ForwardBackend.Model.AccessModel;
import com.ForwadAgency.ForwardBackend.Model.Users;
import com.ForwadAgency.ForwardBackend.Repo.AccessModelRepo;
import com.ForwadAgency.ForwardBackend.Repo.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    AccessModelRepo accessModelRepo;

    public Users addUser(Users user) {

        AccessModel  a= new AccessModel();
        if(user.getBrandName()!=null) {
            a.setSecretKey(user.getSecretKey());
            a.setBrandName(user.getBrandName());
            accessModelRepo.save(a);
        }
        return userRepo.save(user);
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
