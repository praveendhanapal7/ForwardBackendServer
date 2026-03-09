package com.ForwadAgency.ForwardBackend.Repo;

import com.ForwadAgency.ForwardBackend.Model.Leads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadsRepo extends JpaRepository< Leads, String> {
    List<Leads> findAllByClientName(String clientName);
}
