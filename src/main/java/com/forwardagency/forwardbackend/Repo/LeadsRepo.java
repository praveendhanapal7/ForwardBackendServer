package com.forwardagency.forwardbackend.Repo;

import com.forwardagency.forwardbackend.Model.Leads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadsRepo extends JpaRepository<Leads, Integer> {
    List<Leads> findAllByClientName(String clientName);
}
