package io.swagger.IT.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class getAllAccountsSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();


    @And("I get all accounts")
    public void iGetAllAccounts() {

    }

    @Then("I get status {int} in getAllAccountsSteps")
    public void iGetStatusInGetAllAccountsSteps(int arg0) {

    }
}
