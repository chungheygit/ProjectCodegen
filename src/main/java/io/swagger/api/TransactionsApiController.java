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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        return new ResponseEntity<Transaction>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    public ResponseEntity<List<Transaction>> getAllTransactions(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.QUERY, description = "The IBAN number as string", schema=@Schema()) @Valid @RequestParam(value = "iban", required = false) String iban, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date. Format: dd/MM/yyyy HH:mm:ss" ,schema=@Schema()) @Valid @RequestParam(value = "startDateTime", required = false) String startDateTime, @Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date. Format: dd/MM/yyyy HH:mm:ss" ,schema=@Schema()) @Valid @RequestParam(value = "endDateTime", required = false) String endDateTime) throws Exception {
        //Input Iban/starttijd/eindtijd/limit/offset

        // if starttijd is eerder dan eindtijd forbidden. ///if strijd is niet gelijk aan timeformat

        //Input MOET INTEGER ZIJN anders BAD REQUEST It must be a non-negative number
        //DIT IS ZOWEL EMPLOYEE ALS CUSTOMER ENDPOINT
        //CUSTOMER KAN ALLEEN EIGEN TRANSACTIE ZIEN. ANDERE TRANSACTIES UNAUTORIZED
        //ALS NIKS GEVONDEN NOTFOUND

        //if logged in user is customer and iban is not from customer
        if(!userService.IsLoggedInUserEmployee() && !userService.IsIbanFromLoggedInUser(iban)){
            return new ResponseEntity<List<Transaction>>(HttpStatus.UNAUTHORIZED);
        }

        List<Transaction> transactions = new ArrayList<>();

       transactions = transactionService.getTransactionsByFilters(iban, offset, limit, startDateTime, endDateTime);


        if (transactions.size() < 1) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }
    public ResponseEntity<Transaction> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "The transaction ID", required=true, schema=@Schema()) @PathVariable("transactionId") Integer transactionId) {

        //INPUT MOET INTEGER ZIJN anders BAD REQUEST It must be a non-negative number
        //DIT IS ZOWEL EMPLOYEE ALS CUSTOMER ENDPOINT
        //CUSTOMER KAN ALLEEN EIGEN TRANSACTIE ZIEN. ANDERE TRANSACTIES UNAUTORIZED
        //ALS NIKS GEVONDEN NOTFOUND

        //input must be a integer and a positive number
        if (transactionId != (int)transactionId || transactionId < 0){
            return new ResponseEntity<Transaction>(HttpStatus.BAD_REQUEST);
        }

        //Customer kan only view its own transaction, employee can view all transaction
        if(!userService.IsLoggedInUserEmployee() && !transactionService.isTransactionFromLoggedInUser(transactionId)){//methode that checks if transaction is from user
            return new ResponseEntity<Transaction>(HttpStatus.UNAUTHORIZED);
        }


        return new ResponseEntity<Transaction>(transactionService.getTransactionById(transactionId),HttpStatus.OK);

    }
}
