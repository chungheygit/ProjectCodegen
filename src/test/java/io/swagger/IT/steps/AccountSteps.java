package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.jsonwebtoken.Header;
import io.swagger.model.*;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import javassist.bytecode.analysis.Type;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccountSteps {

    private HttpHeaders headers = new HttpHeaders();
    private String baseURL = "http://localhost:8080/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper mapper = new ObjectMapper();
    User user = new User("N'Golo", "Kanté",LocalDate.of(2021,3,18), "emp","password", UserType.ROLE_Employee, new BigDecimal("1000.02"), new BigDecimal("250.02"), false);
    Account account =   new Account(user.getId(), "NL58INHO0123456702", new BigDecimal(7999.25 ), java.time.LocalDate.of(2021,3,18), AccountType.CURRENT, new BigDecimal(500 ), false);



    @When("Employee login")
    public void ILogInWithCorrectCredentials() throws URISyntaxException, JsonProcessingException {
        LoginDTO loginDTO = new LoginDTO();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = new URI(baseURL + "users/login");
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(loginDTO), headers);
        responseEntity = template.postForEntity(uri, entity, String.class);
    }

    @When("I get all accounts")
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void IGetAllAccounts() throws URISyntaxException {
        URI uri = new URI(baseURL + "accounts/");
        headers.setBearerAuth(responseEntity.getBody());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    };


    @Then("Is the status of the request {int}")
    public void isTheStatusOfTheRequest(int expected) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(expected, response);
    }

    @Then("Get a list of accounts back")
    public void GetAListOfAccountsBack() throws JSONException {
        String response = responseEntity.getBody();
        JSONArray jsonArray = new JSONArray(response);
        Assert.assertEquals(1, jsonArray.length());

    }

    @When("I get an account with iban {string}")
    public void IGetAccountWithIban(String iban) throws URISyntaxException {
        URI uri = new URI(baseURL + "accounts/" + iban);
        headers.setBearerAuth(responseEntity.getBody());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        //responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }


    @Then("Get account with iban back {string}")
    public void GetAccountWithIbanBack(String iban) throws JSONException {
        String response = responseEntity.getBody();
        JSONArray jsonArray = new JSONArray(response);
        Assert.assertEquals(iban, jsonArray.length());


    }
    @When("I post an account")
    public void iPostAnAccount() throws JsonProcessingException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        Account account= new Account(1L,"NL58INHO0123456799",new BigDecimal(1000),LocalDate.now(),AccountType.CURRENT,new BigDecimal(500),true );
        URI uri = new URI(baseURL);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(account), headers);
        responseEntity = template.postForEntity(uri, entity, String.class);
    }
}


