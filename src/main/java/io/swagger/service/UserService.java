package io.swagger.service;

import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // Get a user by its ID
    public User getUserById (long id){ return userRepository.findById(id).get(); }

    // Get all users by mail
    public User findUserByEmail(String email) { return userRepository.findUserByEmail(email); }

    public User createUser (User user){
        if(userRepository.findUserByEmail(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        else{
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email/password invalid");
        }
    }

    public User updateUser(User targetUser) { return userRepository.save(targetUser); }

    public String login(LoginDTO loginDTO){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            User user = userRepository.findUserByEmail(loginDTO.getEmail());

            return "Bearer " + jwtTokenProvider.createToken(user.getEmail(), Arrays.asList(user.getUserType()));
        }catch (AuthenticationException exception){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "email/password invalid");
        }

    }

    public Boolean IsLoggedInUserEmployee(){
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        if(currentUser.getUserType() == UserType.ROLE_EMPLOYEE){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean IsIbanFromLoggedInUser(String iban){
        boolean isIbanFromLoggedInUser;
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        try{
            if(accountService.getAccountByIban(iban).getUserId() == currentUser.getId()){
                return true;
            }
        }catch (Exception e){
            log.error("Exception: " + e);
        }
        return false;
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
