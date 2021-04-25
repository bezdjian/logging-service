package hb.loggingservice.it;

import hb.loggingservice.entity.Log;
import hb.loggingservice.model.LogModel;
import hb.loggingservice.model.MessageRequestModel;
import hb.loggingservice.model.MessageResponseModel;
import hb.loggingservice.model.ResponseModel;
import hb.loggingservice.model.ServiceStateModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggingServiceIntegrationTests {

    private static final String BASE_URL = "/message-logging-service";
    private static final Integer MAX_AGE_VALUE = 120;
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    @LocalServerPort
    private int port;

    @Test
    @Order(1)
    @DisplayName("Should return empty list when fetching all logs")
    void shouldRetrieveEmptyList() {
        ResponseEntity<List<Log>> response = testRestTemplate.exchange(createServiceUrl("/logs"),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Log>>() {
            });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("Should add a new message in none existing log")
    void shouldAddNewMessageWithNewLog() {
        final long logId = 1L;
        MessageRequestModel requestModel = MessageRequestModel.builder()
            .name("it test")
            .message("message from it test")
            .logId(logId)
            .build();
        HttpEntity<MessageRequestModel> postBody = new HttpEntity<>(requestModel);
        ResponseEntity<MessageResponseModel> response = testRestTemplate.exchange(createServiceUrl("/message/add"),
            HttpMethod.POST,
            postBody,
            new ParameterizedTypeReference<MessageResponseModel>() {
            });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLogId()).isEqualTo(logId);
        assertThat(response.getBody().getMessage()).isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("Should add max age configuration of 120 seconds")
    void shouldAddMaxAgeConfiguration() {
        final String configParamName = "configurationName";
        final String configParamValue = "MAX_AGE";
        // URI (URL) parameters
        Map<String, Integer> urlParams = new HashMap<>();
        urlParams.put("maxAgeValue", MAX_AGE_VALUE);
        // Query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(createServiceUrl("/configure/{maxAgeValue}"))
            // Add query parameter
            .queryParam(configParamName, configParamValue);

        ResponseEntity<ResponseModel> response = testRestTemplate.exchange(builder.buildAndExpand(urlParams)
                .toUriString(),
            HttpMethod.PUT,
            null,
            new ParameterizedTypeReference<ResponseModel>() {
            });

        final String expectedResponseMessage = "Configuration has been successfully saved for " + configParamValue;
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(expectedResponseMessage);
    }

    @Test
    @Order(4)
    @DisplayName("Should find and return an existing log with a message")
    void shouldFindAndReturnLogWithMessage() {
        final long logId = 1L;
        ResponseEntity<LogModel> response = testRestTemplate.exchange(createServiceUrl("/logs/" + logId),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<LogModel>() {
            });
        System.out.println(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLogId()).isEqualTo(logId);
        assertThat(response.getBody().getMessages()).isNotEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Should return state of the service with information")
    void shouldReturnStateOfTheServiceWithInformation() {
        ResponseEntity<ServiceStateModel> response = testRestTemplate.exchange(createServiceUrl("/"),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ServiceStateModel>() {
            });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMaxAge()).isEqualTo(MAX_AGE_VALUE);
        assertThat(response.getBody().getNumberOfLogs()).isEqualTo(1);
        assertThat(response.getBody().getTotalNumberOfMessages()).isEqualTo(1);
    }

    private String createServiceUrl(String url) {
        return "http://localhost:" + port + BASE_URL + url;
    }
}
