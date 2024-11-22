package by.dudkin.rides.rest.controller;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RideRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    private static final String BASE_URI = "/api/rides";
    private static final String DONE_URI = "/api/rides/2/done";
    private static final String ACTIVATE_URI = "/api/rides/1/activate";
    private static final String CANCEL_URI = "/api/rides/1/cancel";
    private static final String RATE_URI = "/api/rides/2/rate";

    @BeforeEach
    void init() {
        restTemplate.getRestTemplate().setRequestFactory(new JdkClientHttpRequestFactory());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllRides() {
        // Act
        PaginatedResponse<RideResponse> response = restTemplate.getForObject(BASE_URI, PaginatedResponse.class);

        // Assert
        assertNotNull(response);
        assertEquals(10, response.getContent().size());
        assertEquals(0, response.getPage());
    }

    @Test
    void shouldFindRideWithValidId() {
        // Arrange
        String VALID_URI = "%s/%d".formatted(BASE_URI, 1L);

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(VALID_URI, HttpMethod.GET, null, RideResponse.class);

        // Arrange
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(1, requireNonNull(response.getBody()).passengerId()),
            () -> assertEquals(101, requireNonNull(response.getBody()).driverId())
        );
    }

    @Test
    void shouldNotFindRideWithInvalidId() {
        // Arrange
        String INVALID_URI = "%s/%d".formatted(BASE_URI, 999L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(INVALID_URI, HttpMethod.GET, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode())
        );
    }

    @Test
    @Rollback
    void shouldCreateRide() {
        // Arrange
        RideRequest request = TestDataGenerator.randomRideRequest();

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(BASE_URI, HttpMethod.POST, new HttpEntity<>(request), RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode())
        );
    }

    @Test
    void shouldNotCreateRideWhenValidationFails() {
        // Arrange
        RideRequest request = new RideRequest(null, null, null, null, null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(BASE_URI, HttpMethod.POST, new HttpEntity<>(request, headers), ProblemDetail.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode())
        );
    }

    @Test
    @Rollback
    void shouldUpdateRide() {
        // Arrange
        String URI = "%s/%d".formatted(BASE_URI, 1L);
        RideRequest request = TestDataGenerator.randomRideRequest();

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, new HttpEntity<>(request), RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    @Rollback
    void shouldDeleteRide() {
        // Arrange
        String URI = "%s/%d".formatted(BASE_URI, 1L);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
            () -> assertNull(response.getBody())
        );
    }

    @Test
    @Rollback
    void shouldActivateRide() {
        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(ACTIVATE_URI, HttpMethod.PATCH, null, RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(RideStatus.ACTIVE, requireNonNull(response.getBody()).status()),
            () -> assertNotNull(requireNonNull(response.getBody()).startTime())
        );
    }

    @Test
    @Rollback
    void shouldMarkDoneRide() {
        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(DONE_URI, HttpMethod.PATCH, null, RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(RideStatus.DONE, requireNonNull(response.getBody()).status()),
            () -> assertNotNull(requireNonNull(response.getBody()).startTime()),
            () -> assertNotNull(requireNonNull(response.getBody()).endTime())
        );
    }

    @Test
    @Rollback
    void shouldCancelRide() {
        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(CANCEL_URI, HttpMethod.PATCH, null, RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(RideStatus.CANCEL, requireNonNull(response.getBody()).status())
        );
    }

    @Test
    @Rollback
    void shouldRateRide() {
        // Arrange
        RideCompletionRequest request = new RideCompletionRequest(6);

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(RATE_URI, HttpMethod.PATCH, new HttpEntity<>(request), RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(6, requireNonNull(response.getBody()).rating())
        );
    }

}
