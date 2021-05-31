/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transaction;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-17T13:44:26.622Z[GMT]")
@Validated
public interface TransactionsApi {

    @Operation(summary = "Creates a transaction", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Transactions" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Transaction successfully created", content = @Content(schema = @Schema(implementation = Transaction.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/transactions/{iban}",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Transaction> createTransaction(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Transaction body);


    @Operation(summary = "Returns all transactions", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Transactions" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "All transactions are successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/transactions",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> getAllTransactions(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date" ,schema=@Schema()) @Valid @RequestParam(value = "startDateTime", required = false) LocalDate startDateTime, @Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date" ,schema=@Schema()) @Valid @RequestParam(value = "endDateTime", required = false) LocalDate endDateTime);


    @Operation(summary = "Returns a transaction by id", description = "", tags={ "Transactions" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Object with transaction information", content = @Content(schema = @Schema(implementation = Transaction.class))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/transactions/{transactionId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Transaction> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "The transaction ID", required=true, schema=@Schema()) @PathVariable("transactionId") Integer transactionId);


    @Operation(summary = "Returns all transactions from an account", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Transactions" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "All transactions are successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
        
        @ApiResponse(responseCode = "400", description = "Invalid input given"),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized action attempted"),
        
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        
        @ApiResponse(responseCode = "404", description = "Requested object not found") })
    @RequestMapping(value = "/transactions/trigger/{iban}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> getTransactionsByIban(@Pattern(regexp="^NL\\d{2}INHO0\\d{9}$") @Parameter(in = ParameterIn.PATH, description = "The IBAN number as string", required=true, schema=@Schema()) @PathVariable("iban") String iban, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to \\ collect the result set" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(0)@Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date" ,schema=@Schema()) @Valid @RequestParam(value = "startDate", required = false) LocalDate startDate, @Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date" ,schema=@Schema()) @Valid @RequestParam(value = "endDate", required = false) LocalDate endDate);

}

