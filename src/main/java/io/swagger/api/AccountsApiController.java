package io.swagger.api;

import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.UpdateAccountDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

     String bank = "NL01INHO0000000001";

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request, AccountService accountService, UserService userService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.accountService = accountService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('Employee')") // access for employee only
    public ResponseEntity<Account> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody AccountDTO accountDTO) throws Exception {

        accountService.validateBalance(accountDTO.getBalance());
        accountService.validateAbsoluteLimit(accountDTO.getAbsoluteLimit());
        accountService.validateAccounttype(accountDTO.getAccountType());

        return new ResponseEntity<Account>(accountService.createAccount(accountDTO), HttpStatus.CREATED);

    }



    public ResponseEntity<Account> getAccountByIban(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban) throws Exception {
        if(!accountService.validIban(iban)){
            log.error("User entered invalid iban");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid iban entered");
        }
        //employee or owner of iban allowed
        if(!accountService.getAccountByIbanUserAuthorized(iban)){
            log.error("User not authorized");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized");
        }
        else{
            return new ResponseEntity<Account>(accountService.getAccountByIban(iban), HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('Employee')") // access for employee only
    public ResponseEntity<List<Account>> getAllAccounts(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "filter accounts by creation date" ,schema=@Schema()) @Valid @RequestParam(value = "createdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdDate) {

            return new ResponseEntity<List<Account>>((List<Account>) accountService.getAccountsByCreatedDate(createdDate, offset, limit), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Employee')") // access for employee only
    public ResponseEntity<Account> updateAccount(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UpdateAccountDTO updateAccountDTO) throws Exception {

        accountService.validateBalance(updateAccountDTO.getBalance());
        accountService.validateAbsoluteLimit(updateAccountDTO.getAbsoluteLimit());
        accountService.validateAccounttype(updateAccountDTO.getAccountType());

        return new ResponseEntity<Account>(accountService.updateAccount(iban, updateAccountDTO), HttpStatus.OK);

    }

}
