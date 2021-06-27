package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
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

public class LoginSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/users/login";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();

    @When("I log in with email {string} and password {string}")
    public void iLogInWithEmailAndPassword(String email, String password) throws URISyntaxException, JsonProcessingException {
        LoginDTO loginDTO = new LoginDTO(email, password);
        URI uri = new URI(baseUrl);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(loginDTO), headers);
        try{
            responseEntity = template.postForEntity(uri, entity, String.class);
        }
        catch (HttpStatusCodeException ex){
            responseEntity = new ResponseEntity<String>(ex.getMessage(), ex.getStatusCode());
        }

    }

    @Then("I get status {int}")
    public void iGetStatus(int expected) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), expected);
    }


    @And("The response contains a Jwt token")
    public void theResponseContainsAJwtToken() {
        String response = responseEntity.getBody();
        boolean containsBearerTag;
        if(response.contains("Bearer")){
            containsBearerTag = true;
        }
        else{
            containsBearerTag = false;
        }
        Assert.assertEquals(containsBearerTag, true);
    }


    @And("The response is {string}")
    public void theResponseIs(String message) {
        Assert.assertEquals(message, responseEntity.getBody());
    }

    @And("I get message containing {string} in LoginSteps")
    public void iGetMessageContainingInLoginSteps(String message) {
        boolean containsMessage;
        if(responseEntity.getBody().contains(message)){
            containsMessage = true;
        }
        else{
            containsMessage = false;
        }
        Assert.assertEquals(containsMessage, true);
    }
}
