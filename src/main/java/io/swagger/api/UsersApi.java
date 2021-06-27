/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.DTO.UserDTO;
import io.swagger.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")
@Validated
public interface UsersApi {

    @Operation(summary = "Create new user", description = "Employee method, Creates a new user account", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User successfully created", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<User> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody User body);


    @Operation(summary = "Get a list of all the available users", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A list of users", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<User>> getAllUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "filter users by email" ,schema=@Schema()) @Valid @RequestParam(value = "email", required = false) String email) throws Exception;


    @Operation(summary = "Returns a specific user by id", description = "Employee method, or if customer requests own id", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Returned the specified user", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/users/{userId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> getUserById(@Min(0)@Parameter(in = ParameterIn.PATH, description = "Id of a user", required=true, schema=@Schema(allowableValues={  }
)) @PathVariable("userId") Integer userId) throws NotFoundException;


    @Operation(summary = "Login a user", description = "", tags={ "Authentication" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User successfully logged in", content = @Content(schema = @Schema(implementation = String.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/users/login",
        produces = { "text/plain" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<String> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody LoginDTO body);


    @Operation(summary = "Edit specified user information", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User sucessfully updated", content = @Content(schema = @Schema(implementation = User.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/users/{userId}",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<User> updateUser(@Min(0)@Parameter(in = ParameterIn.PATH, description = "Id of a user", required=true, schema=@Schema(allowableValues={  }
)) @PathVariable("userId") Long userId, @Valid @RequestBody UserDTO targetUser);

}

