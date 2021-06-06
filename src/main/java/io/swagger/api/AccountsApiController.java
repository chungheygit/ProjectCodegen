package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.DTO.AccountDTO;

import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;



@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")
@RestController
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final AccountService accountService;

    private final UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request, AccountService accountService, UserService userService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.accountService = accountService;
        this.userService = userService;
    }


    public ResponseEntity<Account> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody AccountDTO accountDTO) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if(!userService.IsLoggedInUserEmployee()) {
                    return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
                }
                return new ResponseEntity<Account>(accountService.createAccount(accountDTO), HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Account>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Account> getAccountByIban(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban) throws Exception {
        return new ResponseEntity<Account>(accountService.getAccountByIbanSecured(iban), HttpStatus.OK);
    }


    public ResponseEntity<List<Account>> getAllAccounts(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "filter accounts by creation date" ,schema=@Schema()) @Valid @RequestParam(value = "createdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdDate) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if(!userService.IsLoggedInUserEmployee()) {
                    return new ResponseEntity<List<Account>>(HttpStatus.UNAUTHORIZED);
                }
                if(createdDate != null)
                {
                    List<Account> a = accountService.getAccountByCreatedDate(createdDate, offset, limit);
                    if (a == null)
                        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
                    return new ResponseEntity<List<Account>>(a, HttpStatus.OK);
                }
                return new ResponseEntity<List<Account>>((List<Account>) accountService.getAccountByCreatedDate(createdDate, offset, limit), HttpStatus.OK);
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Account>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Account> updateAccount(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Account body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if(!userService.IsLoggedInUserEmployee()) {
                    return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
                }
                return new ResponseEntity<Account>(accountService.updateAccount(body), HttpStatus.OK);
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Account>(HttpStatus.NOT_IMPLEMENTED);
    }

}
