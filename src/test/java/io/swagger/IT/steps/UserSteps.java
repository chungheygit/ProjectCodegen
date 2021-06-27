package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.User;
import io.swagger.model.UserType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

public class UserSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseURL = "http://localhost:8080/";
    private String response;
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper mapper = new ObjectMapper();


    @When("I log in as an employee")
    public void ILogInWithCorrectCredentials() throws URISyntaxException, JsonProcessingException {
        LoginDTO loginDTO = new LoginDTO();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = new URI(baseURL + "users/login");
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(loginDTO), headers);
        responseEntity = template.postForEntity(uri, entity, String.class);
    }

    @When("I retrieve all users")
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void iRetrieveAllUsers() throws URISyntaxException {
        URI uri = new URI(baseURL + "users/");
        headers.setBearerAuth(responseEntity.getBody());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    };

    @Then("I should get a {int} http status")
    public void iGetHttpStatus(int status) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), status);
    }

    @Then("I get a list of {int} users")
    public void iGetAListOfUsers(int size) throws JSONException {
        response = responseEntity.getBody();
        JSONArray array = new JSONArray(response);
        Assert.assertEquals(size, array.length());
    }

    @When("I retrieve user by {int} id ")
    public void iRetrieveUserById(int id) throws URISyntaxException {
        URI uri = new URI(baseURL + id);
        responseEntity = template.getForEntity(uri, String.class);
    }

    @Then("I should get a {string} firstname")
    public void iGetFirstName(String firstName) throws JSONException {
        response = responseEntity.getBody();
        Assert.assertEquals(firstName,
                new JSONObject(response)
                        .getString("firstName"));
    }

    @When("I post a user")
    public void iPostAUser() throws JsonProcessingException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User("N'Golo", "Kanté", LocalDate.of(2021, 3, 18), "emp", "password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), false);
        URI uri = new URI(baseURL);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(user), headers);
        responseEntity = template.postForEntity(uri, entity, String.class);
    }
}


