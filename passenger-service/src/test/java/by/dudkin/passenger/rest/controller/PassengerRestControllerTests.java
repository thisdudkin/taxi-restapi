package by.dudkin.passenger.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.passenger.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PassengerRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    private static final String PASSENGERS_URI = "/passengers";

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllPassengers() {
        // Act
        PaginatedResponse<PassengerResponse> response = restTemplate.getForObject(PASSENGERS_URI, PaginatedResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getContent().size()).isGreaterThan(5);
    }

    @Test
    void shouldFindPassengerWithValidId() {
        // Arrange
        var URI = "%s/%d".formatted(PASSENGERS_URI, 3L);

        // Act
        ResponseEntity<PassengerResponse> response = restTemplate.exchange(URI, HttpMethod.GET, null, PassengerResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).info()).isNotNull();
    }

    @Test
    void shouldNotFindPassengerWithInvalidId() {
        // Arrange
        var URI = "%s/%d".formatted(PASSENGERS_URI, 999L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldCreatePassenger() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();

        // Act
        ResponseEntity<PassengerResponse> response = restTemplate.exchange(PASSENGERS_URI, HttpMethod.POST, new HttpEntity<>(request), PassengerResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).info().getFirstName()).isEqualTo(request.info().getFirstName());
    }

    @Test
    void shouldNotCreatePassengerWhenValidationFails() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequestWithInfo(null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(PASSENGERS_URI, HttpMethod.POST, new HttpEntity<>(request, headers), ProblemDetail.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldUpdatePassenger() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();
        String URI = "%s/%d".formatted(PASSENGERS_URI, 6L);

        // Act
        ResponseEntity<PassengerResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, new HttpEntity<>(request), PassengerResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).info().getFirstName()).isEqualTo(request.info().getFirstName());
    }

    @Test
    void shouldDeletePassenger() {
        // Arrange
        var URI = "%s/%d".formatted(PASSENGERS_URI, 6L);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
