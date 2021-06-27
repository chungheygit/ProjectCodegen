package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.DTO.UserDTO;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final UserService userService;

    private final AccountService accountService;

    //private final Account bankAccount;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, UserService userService, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userService = userService;
        this.accountService = accountService;
        //this.bankAccount = accountService.GetAllAccounts().stream().filter(b -> b.getIban()=="NL01INHO0000000001").findAny().get();
    }

    // CREATE A NEW USER
   // @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        //Create User
        try {
            if(!userService.IsLoggedInUserEmployee()) {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<User>(userService.createUser(body),HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // GET ALL USERS
    public ResponseEntity<List<User>> getAllUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "filter users by email" ,schema=@Schema()) @Valid @RequestParam(value = "email", required = false) String email) throws Exception {
        String accept = request.getHeader("Accept");

        List<User> users = new ArrayList<User>();
        try {

            // GET ALL USERS BY MAIL
            if (email != null) {
                User u = userService.findUserByEmail(email);
                //users = new ArrayList<User>();
                if(!userService.IsLoggedInUserEmployee()) {
                    return new ResponseEntity<List<User>>(HttpStatus.UNAUTHORIZED);
                }
                if (u == null)
                    return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
                users.add(u);
                return new ResponseEntity<List<User>>(users, HttpStatus.OK);
            }
            //RETURN USERS WITH OR WITHOUT LIMIT AND OFFSET
            return new ResponseEntity<List<User>>(userService.getAllUsers(limit, offset), HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // GET A USER BY ITS ID
    public ResponseEntity<User> getUserById(@Min(0)@Parameter(in = ParameterIn.PATH, description = "Id of a user", required=true, schema=@Schema(allowableValues={  }
)) @PathVariable("userId") Integer userId) {
        String accept = request.getHeader("Accept");
        //Get user by id
        try {
            if(!userService.IsLoggedInUserEmployee()) {
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<User>(userService.getUserById(userId),HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<String> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody LoginDTO body) {
        return new ResponseEntity<String>(userService.login(body), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('Employee')")
    public ResponseEntity<User> updateUser(@Min(0)@Parameter(in = ParameterIn.PATH, description = "Id of a user", required=true, schema=@Schema(allowableValues={  }
)) @PathVariable("userId") Long userId, @Valid @RequestBody UserDTO targetUser) {

        // check if userId is a long and not negative
        // Mag niet UserId van ons bank zijn
        // target user ophalen
        //targetuser updaten maar valideer eerst alle dingen


        //DIT IS HARDCODED, DIT MOET IK VERANDERN NAAR CONSTANTE OFZO
        if(!userService.IsLoggedInUserEmployee() || userId==0) {
            return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        }

        //input must be a Long and a positive number
        if (userId != (long)userId || userId < 0){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

        //check if user exist
        if(userService.getUserById(userId) == null){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        //try to update user
//        try{
            return new ResponseEntity<User>(userService.updateUser(userId,targetUser), HttpStatus.OK);
//        }
//        catch (Exception e){
//            return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
//        }
    }
}
