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
//    # voor het runnen van deze testen moet er eerst code gecomment worden.REGEL 87
//    # voor het runnen van deze testen moet er eerst code gecomment worden.REGEL 87
//    # voor het runnen van deze testen moet er eerst code gecomment worden.REGEL 87
//    # voor het runnen van deze testen moet er eerst code gecomment worden.REGEL 87


    @When("Employee log in")
    public void ILogInWithCorrectCredentials() throws URISyntaxException, JsonProcessingException {
        LoginDTO loginDTO = new LoginDTO();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = new URI(baseURL + "users/login");
        HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(loginDTO), headers);
        responseEntity = template.postForEntity(uri, entity, String.class);
    }

    @When("Ik alle accounts ophaal")
    @WithMockUser(username = "emp", password = "password", roles = "Employee")
    public void ikAlleAccountsOphaal() throws URISyntaxException {
        URI uri = new URI(baseURL + "accounts/");
        headers.setBearerAuth(responseEntity.getBody());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    };


    @Then("Is de status van het request {int}")
    public void isDeStatusVanHetRequest(int expected) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(expected, response);
    }

    @Then("Krijg een lijst terug van accounts")
    public void krijgEenLijstTerugVanAccounts() throws JSONException {
        String response = responseEntity.getBody();
        JSONArray jsonArray = new JSONArray(response);
        Assert.assertEquals(2, jsonArray.length());

    }

    @When("Ik een Account ophaal met iban {string}")
    public void ikEenAccountOphaalMetIban(String iban) throws URISyntaxException {
        URI uri = new URI(baseURL + "accounts/" + iban);
        headers.setBearerAuth(responseEntity.getBody());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        //responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("Is het account absoluteLimit {double}")
    public void isHetAccountAbsoluteLimit(double absoluteLimit) throws JSONException {
        String response = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(response);
        Assert.assertEquals(absoluteLimit, jsonObject.getDouble("absoluteLimit"), 0);
    }

    @When("Ik een Account ophaal met Username {string}")
    public void ikEenAccountOphaalMetUsername(String username) throws URISyntaxException {
        URI uri = new URI(baseURL + "accounts/" + "username/" + username);
        headers.setBearerAuth(responseEntity.getBody());
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);

    }

    @Then("Is het iban van het account {string}")
    public void isHetIbanVanHetAccount(String iban) throws JSONException {
        String response = responseEntity.getBody();
        JSONArray jsonArray = new JSONArray(response);
        Assert.assertEquals(iban, jsonArray.getJSONObject(0).getString("iban"));
    }


    @Then("Ik een Account maak met AbsoluteLimit {double} en owner {string} en type {string} en active Status.Active")
    public void ikEenAccountMaakMetAbsoluteLimitEnOwnerEnTypeEnActiveStatusActive(double absoluteLimit, String owner, String type) throws URISyntaxException, JsonProcessingException {

        mapper = new ObjectMapper();
        URI uri = new URI(baseURL + "accounts");
        headers.setBearerAuth(responseEntity.getBody());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(account), headers);
        responseEntity = template.exchange(uri, HttpMethod.POST, entity, String.class);

    }




}


