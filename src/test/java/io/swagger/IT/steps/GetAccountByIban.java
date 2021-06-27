package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.DTO.LoginDTO;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class GetAccountByIban {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();

    @When("I get account by iban {string}")
    public void iGetAccountByIban(String iban) throws URISyntaxException {
        URI uri = new URI(baseUrl + "accounts/" + iban);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.getForEntity(uri, String.class);
    }

    @Then("I get status {int} in GetAccountByIbanSteps")
    public void iGetStatusInGetAccountByIbanSteps(int expected) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), expected);
    }

    @When("I log in with email {string} and password {string} for GetAccountByIbanSteps")
    public void iLogInWithEmailAndPasswordForGetAccountByIbanSteps(String email, String password) throws JsonProcessingException, URISyntaxException {
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
}
