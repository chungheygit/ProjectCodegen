package io.swagger.service;

import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.DTO.UserDTO;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.model.UserType;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.security.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService() {
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
    public User getUserById (long id){
        return userRepository
            .findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("User not found"));
    }

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

    public User updateUser(long targetUserId, UserDTO targetUser) {
        //methode validates user input
        validateUserInput(targetUser);
        //get user
        User userToUpdate = getUserById(targetUserId);

        userToUpdate.setFirstName(targetUser.getFirstName());
        userToUpdate.setLastName(targetUser.getLastName());
        userToUpdate.setDayLimit(targetUser.getDayLimit());
        userToUpdate.setPassword(targetUser.getPassword());
        userToUpdate.setDateOfBirth(targetUser.getDateOfBirth());
        userToUpdate.setEmail(targetUser.getEmail());
        userToUpdate.setOpen(targetUser.getOpen());
        userToUpdate.setTransactionLimit(targetUser.getTransactionLimit());
        userToUpdate.setUserType(targetUser.getUserTypeEnum());
        return userRepository.save(userToUpdate);
    }

    private void validateUserInput(UserDTO targetUser){

        //require a string
        if(!targetUser.getFirstName().matches("[a-zA-Z_]+") || !targetUser.getLastName().matches("[a-zA-Z_]+")) {
            log.error("Invalid input given, String required!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input given, String required.");
        }

        //checks email with regex
        if(!targetUser.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            log.error("Invalid email given, check email again!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email given, check email again!.");
        }

        //checks DateOfBirth with regex and DateOfBirth must be a date in the past
        if(!targetUser.getDateOfBirth().isBefore(LocalDate.now()) /*|| targetUser.getDateOfBirth()==*/){
            log.error("Invalid DateOfBirth given, LocalDate required!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid DateOfBirth given, LocalDate required!.");
        }

        //checks enum
        //if(targetUser.getUserTypeEnum().fromValue(UserType))

        //transactionlimit and daylimit can not be negative
        if(targetUser.getTransactionLimit().compareTo(BigDecimal.ZERO) < 0 || targetUser.getDayLimit().compareTo(BigDecimal.ZERO) < 0){
            log.error("Invalid limit given, Transactionlimit or daylimit can not be negative!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid limit given, Transactionlimit or daylimit can not be negative!");
        }

        //check password
    }

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
        if(currentUser.getUserType() == UserType.ROLE_Employee){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean IsIbanFromLoggedInUser(String iban){
        User currentUser = findUserByEmail(myUserDetailsService.getLoggedInUser().getUsername());
        try{
            if(accountService.getAccountByIban(iban).getUser().getId() == currentUser.getId()){
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
