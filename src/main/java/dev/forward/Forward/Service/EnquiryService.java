package dev.forward.Forward.Service;

import dev.forward.Forward.EnquiryRepo;
import dev.forward.Forward.Model.Enquiry;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EnquiryService
{
    @Autowired
    EnquiryRepo enquiryRepo;

    public Enquiry addEnquiry(Enquiry enquiry)
    {
        enquiryRepo.save(enquiry);
        return enquiry;
    }

    public List<Enquiry> listAllEnquiries()
    {
      return enquiryRepo.findAll();
    }

    public List<Enquiry> getEnquiriesByClient(String clientName)
    {
        return enquiryRepo.findAllByClientName(clientName);
    }



}
