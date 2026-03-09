package com.ForwadAgency.ForwardBackend.Repo;

import com.ForwadAgency.ForwardBackend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


    @Repository
    public interface UserRepo extends JpaRepository<Users, String>
    {
        Users findByEmail(String email);
    }
