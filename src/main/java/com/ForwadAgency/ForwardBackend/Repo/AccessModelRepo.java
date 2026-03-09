package com.ForwadAgency.ForwardBackend.Repo;

import com.ForwadAgency.ForwardBackend.Model.AccessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessModelRepo extends JpaRepository<AccessModel , String>
{
    AccessModel getAccessModelBySecretKey(String secretKey);
}
