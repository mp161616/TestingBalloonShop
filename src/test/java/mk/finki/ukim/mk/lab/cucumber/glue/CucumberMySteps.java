package mk.finki.ukim.mk.lab.cucumber.glue;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.log4j.Log4j2;
import mk.finki.ukim.mk.lab.model.Balloon;
import mk.finki.ukim.mk.lab.model.Manufacturer;
import mk.finki.ukim.mk.lab.model.User;
import mk.finki.ukim.mk.lab.model.enumerations.Role;
import mk.finki.ukim.mk.lab.repository.jpa.UserRepository;
import mk.finki.ukim.mk.lab.service.BalloonService;
import mk.finki.ukim.mk.lab.service.UserService;
import mk.finki.ukim.mk.lab.service.impl.BalloonServiceImpl;
import mk.finki.ukim.mk.lab.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest()
@CucumberContextConfiguration
@Log4j2
public class CucumberMySteps {

    ResponseEntity<String> lastResponse;
    RestTemplate restTemplate;



    @When("the client calls endpoint {string}")
    public void whenClientCalls(String url) {
        try {
            lastResponse = new RestTemplate().exchange("http://localhost:8080"  + url, HttpMethod.GET, null,
                    String.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();
        }

    }

    @Then("response status code is {int}")
    public void thenStatusCodee(int expected) {
        Assertions.assertNotNull(lastResponse);
        Assertions.assertNotNull(lastResponse.getStatusCode());
        assertThat("status code is" + expected,
                lastResponse.getStatusCodeValue() == expected);
    }

    @Then("response status is not present")
    public void thenStatusCodeeIsNotPresent() {
        Assertions.assertNull(lastResponse);

    }

    @Then("returned string should have a body")
    public void thenStringIs() {
        Assertions.assertEquals(true, lastResponse.hasBody());
    }

}
