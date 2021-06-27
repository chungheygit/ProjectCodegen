package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.AccountType;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.TransactionDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class AccountSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();

    @And("I create an account with userId {int} and open {string} and balance {int} and absoluteLimit {int} and accountType {string}")
    public void iCreateAnAccountWithUserIdAndOpenAndBalanceAndAbsoluteLimitAndAccountType(Long userId, Boolean open, double balance , double absoluteLimit, AccountType accountType) throws URISyntaxException, JsonProcessingException {
        AccountDTO accountDTO = new AccountDTO(userId,open,new BigDecimal(balance),new BigDecimal(absoluteLimit),accountType);
        URI uri = new URI(baseUrl + "accounts/");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(accountDTO), headers);
        try{
            responseEntity = template.postForEntity(uri, entity, String.class);
        }
        catch (HttpClientErrorException ex){
            responseEntity = new ResponseEntity<String>(ex.getMessage(), ex.getStatusCode());
        }
    }

    @When("I log in with email {string} and password {string} for account")
    public void iLogInWithEmailAndPasswordForAccount(String arg0, String arg1) {
    }

    @Then("I get status {int} in AccountSteps")
    public void iGetStatusInAccountSteps(int arg0) {
    }


}
