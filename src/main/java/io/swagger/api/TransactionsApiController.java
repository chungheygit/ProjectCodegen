package io.swagger.api;


import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.Account;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;

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
    private AccountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request, TransactionService service, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.transactionService = service;
        this.accountService=accountService;
    }

    public ResponseEntity<Transaction> createTransaction(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody TransactionDTO transactionDTO) throws Exception {
        return new ResponseEntity<Transaction>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    public ResponseEntity<List<Transaction>> getAllTransactions(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.QUERY, description = "The IBAN number as string", schema=@Schema()) @Valid @RequestParam(value = "iban", required = false) String iban, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
    )) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date" ,schema=@Schema()) @Valid @RequestParam(value = "startDateTime", required = false) String startDateTime, @Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date" ,schema=@Schema()) @Valid @RequestParam(value = "endDateTime", required = false) String endDateTime) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
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

//    public ResponseEntity<List<Transaction>> getTransactionsByIban(
//            @Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban,
//            @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }))
//            @Valid @RequestParam(value = "offset", required = false) Integer offset,
//            @Min(1)@Max(100)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }))
//            @Valid @RequestParam(value = "limit", required = false) Integer limit,
//            @Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date" ,schema=@Schema())
//            @Valid @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
//            @Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date" ,schema=@Schema())
//                @Valid @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                //defaultValue = "#{T(java.time.LocalDateTime).now()}"
//                if(startDate==null){
//                    startDate = LocalDate.of(1900,01,01);
//                }
//                if(endDate==null){
//                    endDate = LocalDate.now();
//                }
//                return new ResponseEntity<List<Transaction>>(transactionService.getTransactionsByIban(iban, offset, limit, startDate, endDate), HttpStatus.OK);
//            } catch (Exception e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
//
//        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
//    }

}
