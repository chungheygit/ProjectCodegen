package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class CreateTransactionSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();


    @And("I create a transaction with sender {string} and receiver {string} and amount {int} and description {string}")
    public void iCreateATransactionWithSenderAndReceiverAndAmountAndDescription(String sender, String receiver, double amount, String description) throws JsonProcessingException, URISyntaxException {
        TransactionDTO transactionDTO = new TransactionDTO(sender, receiver, new BigDecimal(amount), description);
        URI uri = new URI(baseUrl + "transactions/");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(transactionDTO), headers);
        try{
            responseEntity = template.postForEntity(uri, entity, String.class);
        }
        catch (HttpClientErrorException ex){
            responseEntity = new ResponseEntity<String>(ex.getMessage(), ex.getStatusCode());
        }
    }

    @When("I log in with email {string} and password {string} for transaction")
    public void iLogInWithEmailAndPasswordForTransaction(String email, String password) throws JsonProcessingException, URISyntaxException {
        LoginDTO loginDTO = new LoginDTO(email, password);
        URI uri = new URI(baseUrl + "users/login");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(loginDTO), headers);
        try{
            responseEntity = template.postForEntity(uri, entity, String.class);
            headers.add("Authorization", responseEntity.getBody());
        }
        catch (HttpStatusCodeException ex){
            responseEntity = new ResponseEntity<String>(ex.getMessage(), ex.getStatusCode());
        }
    }


    @Then("I get status {int} in CreateTransactionSteps")
    public void iGetStatusInCreateTransactionSteps(int expected) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), expected);
    }

    @And("I get body {string} in CreateTransactionSteps")
    public void iGetBodyInCreateTransactionSteps(String expected) {
        Assert.assertEquals(responseEntity.getBody(), expected);
    }

    @And("I get message containing {string} in CreateTransactionSteps")
    public void iGetMessageContainingInCreateTransactionSteps(String message) {
        boolean containsMessage;
        if(responseEntity.getBody().contains(message)){
            containsMessage = true;
        }
        else{
            containsMessage = false;
        }
        Assert.assertEquals(containsMessage, true);
    }

    @And("I get transaction with transactiontype withdrawal in CreateTransactionSteps")
    public void iGetTransactionWithTransactiontypeWithdrawalInCreateTransactionSteps() throws JSONException {
        JSONObject transaction = new JSONObject(responseEntity.getBody());
        TransactionType transactionType = TransactionType.fromValue(transaction.get("transactionType").toString());
        Assert.assertEquals(transactionType, TransactionType.WITHDRAWAL);
    }
}
