package io.swagger.service;

import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    // Get all users (With limit en offset)
    public List<User> getAllUsers(Integer limit, Integer offset) throws Exception {
        List<User> allUsers = userRepository.findAll();

        if(allUsers.size() == 0){   	return allUsers;       } //No users found

        //apply pagination with/to offset and limit
        allUsers = createPageable(offset, limit, allUsers);

        return allUsers;
    }

    // Create a new User
    public User createUser (User user){ return userRepository.save(user);}

    // Get a user by its ID
    public User getUserById (long id){ return userRepository.findById(id).get(); }

    // Get all users by mail
    public User findUserByEmail(String email)
    {
        return userRepository.findUserByEmail(email);
    }

    /**
     * Apply Pagination
     * @param offset
     * @param limit
     * @param allUsers
     * @return
     * @throws Exception
     */
    private List<User> createPageable(Integer offset, Integer limit, List<User> allUsers) throws Exception{

        if(limit == null && offset == null){
            // No pagination
            return allUsers;
        }

        int size = allUsers.size();
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = size;
        }
        if (limit <= 0) {
            throw new Exception("limit can't be zero or negative");
        }

        if (offset < 0) {
            throw new Exception("offset can't be  negative");
        }

        limit = offset + limit ;

        if(limit > size) { limit  = size;}
        if(offset > size){ offset = size;}

        allUsers= allUsers.subList(offset, limit);

        return allUsers;
    }

}
