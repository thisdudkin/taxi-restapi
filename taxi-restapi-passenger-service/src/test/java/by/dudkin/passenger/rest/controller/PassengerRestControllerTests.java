package by.dudkin.passenger.rest.controller;

import by.dudkin.common.util.KafkaConstants;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.passenger.util.TestDataGenerator;
import by.dudkin.passenger.util.TestJwtUtils;
import by.dudkin.passenger.util.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static by.dudkin.passenger.util.TestJwtUtils.createHeadersWithToken;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@ActiveProfiles({"test", "kafka"})
@Sql("classpath:data.sql")
@Import(TestSecurityConfig.class)
@EmbeddedKafka(partitions = 1, topics = {KafkaConstants.PASSENGER_ACCOUNT_REQUESTS_TOPIC})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PassengerRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    private static final String PASSENGERS_URI = "/api/passengers";

    @Test
    void shouldReturns401HttpStatusCode() {
        // Act
        var URI = "%s/%s".formatted(PASSENGERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, null, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(401));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
    }

    @Test
    void shouldReturn403HttpStatusCode() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_DRIVER);
        HttpEntity<PassengerRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(PASSENGERS_URI, HttpMethod.POST, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldFindAllPassengers() {
        // Arrange
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<PaginatedResponse> response = restTemplate.exchange(PASSENGERS_URI, HttpMethod.GET, entity, PaginatedResponse.class);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertThat(response.getBody().getContent().size()).isGreaterThan(5);
    }

    @Test
    void shouldFindPassengerWithValidId() {
        // Arrange
        var URI = "%s/%s".formatted(PASSENGERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<PassengerResponse> response = restTemplate.exchange(URI, HttpMethod.GET, entity, PassengerResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).info()).isNotNull();
    }

    @Test
    void shouldNotFindPassengerWithInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(PASSENGERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, entity, ProblemDetail.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldCreatePassenger() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<PassengerRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<PassengerResponse> response = restTemplate.exchange(PASSENGERS_URI, HttpMethod.POST, entity, PassengerResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).info().getFirstName()).isEqualTo(request.info().getFirstName());
    }

    @Test
    void shouldNotCreatePassengerWhenValidationFails() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequestWithInfo(null);
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<PassengerRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(PASSENGERS_URI, HttpMethod.POST, entity, ProblemDetail.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldUpdatePassenger() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();
        String URI = "%s/%s".formatted(PASSENGERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6986");
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<PassengerRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<PassengerResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, entity, PassengerResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).info().getFirstName()).isEqualTo(request.info().getFirstName());
    }

    @Test
    void shouldDeletePassenger() {
        // Arrange
        var URI = "%s/%s".formatted(PASSENGERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6986");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, entity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
