package dev.forward.Forward.Service;

import dev.forward.Forward.AgencyRepo;
import dev.forward.Forward.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AgencyRepo agencyRepo;

    public User createUser(User user)
    {
        agencyRepo.save(user);
       return  user;
    }

}
