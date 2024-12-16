package by.dudkin.rides.rest.controller;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.rest.feign.DriverClient;
import by.dudkin.rides.rest.feign.PassengerClient;
import by.dudkin.rides.rest.feign.PaymentClient;
import by.dudkin.rides.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

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
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"ride-requests"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RideRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    private PassengerClient passengerClient;

    @MockBean
    private PaymentClient paymentClient;

    @MockBean
    private DriverClient driverClient;

    private static final String BASE_URI = "/api/rides";
    private static final String DONE_URI = "/api/rides/862eb8bc-8d7e-4a44-9dd2-cc258faf6983/done";
    private static final String ACTIVATE_URI = "/api/rides/862eb8bc-8d7e-4a44-9dd2-cc258faf6982/activate";
    private static final String CANCEL_URI = "/api/rides/862eb8bc-8d7e-4a44-9dd2-cc258faf6983/cancel";
    private static final String RATE_URI = "/api/rides/862eb8bc-8d7e-4a44-9dd2-cc258faf6984/rate";

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
        String VALID_URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(VALID_URI, HttpMethod.GET, null, RideResponse.class);

        // Arrange
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertNotNull(requireNonNull(response.getBody()).passengerId()),
            () -> assertNotNull(requireNonNull(response.getBody()).driverId())
        );
    }

    @Test
    void shouldNotFindRideWithInvalidId() {
        // Arrange
        String INVALID_URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
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
        BalanceResponse<UUID> mockResponse = new BalanceResponse<>(request.passengerId(), BigDecimal.valueOf(1000));
        Mockito.when(passengerClient.checkBalance(request.passengerId())).thenReturn(mockResponse);

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(BASE_URI, HttpMethod.POST, new HttpEntity<>(request), RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
            () -> assertEquals(request.passengerId(), requireNonNull(response.getBody()).passengerId())
        );
    }

    @Test
    void shouldNotCreateRideWhenValidationFails() {
        // Arrange
        RideRequest request = new RideRequest(null, null, null, null);
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
        String URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6984");
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
        String URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

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
        // Arrange
        Mockito.doNothing().when(paymentClient).processTransaction(Mockito.any(TransactionRequest.class));
        Mockito.doNothing().when(driverClient).markDriverAvailable(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6989"));

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
