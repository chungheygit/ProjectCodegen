package io.swagger.api;

import io.swagger.model.DTO.LoginDTO;
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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final UserService userService;

    private final AccountService accountService;




    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, UserService userService, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userService = userService;
        this.accountService = accountService;
    }

    // CREATE A NEW USER
    // Access will only be allowed for users with the role "ROLE_EMPLOYEE"
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<User> createUser(
            @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema())
            @Valid
            @RequestBody User body) {
        //Create User
        if (!body.getEmail().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            //(FORBIDDEN) A 403 - Invalid format A validation error is an error due to invalid client requests. (Endpoint validation rules are not matched  - e.g. regex constraint doesn't match)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid email");
        }else if (userService.findUserByEmail(body.getEmail()) != null) {
            // (FORBIDDEN) A 403  - Already Exists error indicates that it is not possible to create a resource with the given definition because another resource already exists with the same attributes
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already exists with email");
        }
            return new ResponseEntity<User>(userService.createUser(body),HttpStatus.CREATED);
    }

    // GET ALL USERS
    // Access will only be allowed for users with the role "ROLE_EMPLOYEE"
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<List<User>> getAllUsers(
            @Min(0)
            @Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }))
            @Valid
            @RequestParam(value = "offset", required = false) Integer offset,
            @Min(0)
            @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }))
            @Valid
            @RequestParam(value = "limit", required = false) Integer limit,
            @Parameter(in = ParameterIn.QUERY, description = "filter users by email" ,schema=@Schema())
            @Valid
            @RequestParam(value = "email", required = false) String email) throws Exception {
        try {
            if (email != null) {
                //Get user by email
                User user = userService.findUserByEmail(email);
                List <User> users = new ArrayList<User>();
                // If there is no user with this email then..
                if (user == null)
                    // (NOTACCEPTABLE) 406: 406 Not Acceptable client error response code indicates that the server cannot produce a response matching the list of acceptable values defined in the request's proactive content negotiation headers
                    return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
                users.add(user);
                return new ResponseEntity<List<User>>(users, HttpStatus.OK);
            }
            return new ResponseEntity<List<User>>(userService.getAllUsers(limit, offset), HttpStatus.OK);
        } catch (ResponseStatusException iae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // GET A USER BY ITS ID
    // Access will only be allowed for users with the role "ROLE_EMPLOYEE"
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<User> getUserById(
            @Min(0)
            @Parameter(in = ParameterIn.PATH, description = "Id of a user", required=true, schema=@Schema(allowableValues={  }))
            @PathVariable("userId") Integer userId) throws NotFoundException {
        //Get user by id
        return new ResponseEntity<User>(userService.getUserById(userId),HttpStatus.OK);
    }

    public ResponseEntity<String> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody LoginDTO body) {
        return new ResponseEntity<String>(userService.login(body), HttpStatus.OK);
    }

    public ResponseEntity<User> updateUser(@Min(0)@Parameter(in = ParameterIn.PATH, description = "Id of a user", required=true, schema=@Schema(allowableValues={  }
)) @PathVariable("userId") Long userId, @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            if(!userService.IsLoggedInUserEmployee()){
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }
            //userid of our bank is 0
            if(userId==0){
                return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
            }
            try {
                    User targetUser = userService.getUserById(userId);

            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
            body.setId(Long.valueOf(userId.longValue()));
            return new ResponseEntity<User>(userService.updateUser(body), HttpStatus.OK);
        }

        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }

}
