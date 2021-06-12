package io.swagger.api;

import io.swagger.annotations.ApiParam;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.service.TransactionService;

import io.swagger.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.model.Transaction;
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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")
@RestController
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private TransactionService transactionService;
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request, TransactionService service, UserService userService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.transactionService = service;
        this.userService=userService;
    }

    public ResponseEntity<Transaction> createTransaction(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody TransactionDTO transactionDTO) throws Exception {
        if(!transactionService.IsUserPerformingIsPermitted(transactionDTO.getSender())){
            log.error("User not employee, and not the owner of sending account");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not the sender");
        }
        else {
            return new ResponseEntity<Transaction>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
        }
    }

    public ResponseEntity<List<Transaction>> getAllTransactions(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.QUERY, description = "The IBAN number as string", schema=@Schema()) @Valid @RequestParam(value = "iban", required = false) String iban, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date" ,schema=@Schema()) @Valid @RequestParam(value = "startDateTime", required = false) String startDateTime, @Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date" ,schema=@Schema()) @Valid @RequestParam(value = "endDateTime", required = false) String endDateTime) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
                return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
            }
            try {
                return new ResponseEntity<List<Transaction>>(transactionService.getTransactionsByFilters(iban, offset, limit, startDateTime, endDateTime), HttpStatus.OK);

            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }
    public ResponseEntity<Transaction> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "The transaction ID", required=true, schema=@Schema()) @PathVariable("transactionId") Integer transactionId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(transactionService.getTransactionById(transactionId),HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Transaction>(HttpStatus.NOT_IMPLEMENTED);
    }
}
