package dev.forward.Forward;

import dev.forward.Forward.Model.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepo extends JpaRepository<Enquiry, Integer> {
    List<Enquiry> findAllByClientNameIgnoreCase(String clientName);
    List<Enquiry> findAllByClientName(String clientName);
}
