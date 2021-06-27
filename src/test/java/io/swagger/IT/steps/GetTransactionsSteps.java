package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.DTO.LoginDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;

public class GetTransactionsSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void login() throws JsonProcessingException, URISyntaxException {
        LoginDTO loginDTO = new LoginDTO("emp", "password");
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

    @When("I get transactions by iban")
    public void iGetTransactionsByIban() throws JsonProcessingException, URISyntaxException {
        login();

        URI uri = new URI(baseUrl + "transaction/NL58INHO0123456701");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        responseEntity = template.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    @Then("I get status {int} in GetTransactionsSteps")
    public void iGetStatusInGetTransactionsSteps(int expected) {
        Assert.assertEquals(responseEntity.getStatusCodeValue(), expected);
    }

    @And("I get array of transactions by iban")
    public void iGetArrayOfTransactionsByIban() throws JSONException {
        JSONArray jsonArray = new JSONArray(responseEntity.getBody());
        Assert.assertTrue(jsonArray.length() > 1);
    }

    @When("I get transactions by iban with {string} {int}")
    public void iGetTransactionsByIbanWith(String parameter, int limit) throws JsonProcessingException, URISyntaxException {
        login();

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(String.valueOf(baseUrl+ "transaction/NL58INHO0123456701"))
                .queryParam(parameter, limit);

        responseEntity = template.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, String.class);
    }

    @And("I get array with length of {int} transactions by iban")
    public void iGetArrayWithLengthOfTransactionsByIban(int length) throws JSONException {
        JSONArray jsonArray = new JSONArray(responseEntity.getBody());
        Assert.assertEquals(length, jsonArray.length());
    }

    @When("I get transactions by iban with StartDateTime {string}")
    public void iGetTransactionsByIbanWithStartDateTime(String startDate) throws JSONException {
//        Timestamp timestampExpected = convertToTimestamp(startDate);
//        Timestamp timestampActual;
//
//        JSONArray jsonArray = new JSONArray(responseEntity.getBody());
//        for (int i = 0; i < jsonArray.length(); i++) {
//            timestampActual = Timestamp.valueOf(jsonArray.getJSONObject(i).getString("dateAndTime").replace('T', ' ').substring(0, 19));
//            Assert.assertTrue(timestampActual.after(timestampExpected) || timestampActual.equals(timestampExpected));
//        }
    }

    @And("I get array of transactions by iban after {string}")
    public void iGetArrayOfTransactionsByIbanAfter(String arg0) {
    }

    @When("I get transactions by iban with EndDateTime {string}")
    public void iGetTransactionsByIbanWithEndDateTime(String arg0) {
    }

    @And("I get array of transactions by iban before {string}")
    public void iGetArrayOfTransactionsByIbanBefore(String arg0) {
    }
}
