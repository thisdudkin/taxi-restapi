package by.dudkin.rides.rest.controller;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.feign.DriverClient;
import by.dudkin.rides.feign.PassengerClient;
import by.dudkin.rides.feign.PaymentClient;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.PassengerResponse;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.util.TestDataGenerator;
import by.dudkin.rides.util.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import java.util.UUID;

import static by.dudkin.rides.util.TestJwtUtils.ROLE_ADMIN;
import static by.dudkin.rides.util.TestJwtUtils.ROLE_DRIVER;
import static by.dudkin.rides.util.TestJwtUtils.ROLE_PASSENGER;
import static by.dudkin.rides.util.TestJwtUtils.createHeadersWithTokenAndUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"ride-requests"})
@Import({TestSecurityConfig.class})
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
    void shouldReturn401HttpStatusCode() {
        // Arrange
        String URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6983");

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, null, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldReturn403HttpStatusCode() {
        // Arrange
        String URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6983");
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_PASSENGER);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.DELETE, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldFindAllRides() {
        // Act
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_PASSENGER);
        ResponseEntity<PaginatedResponse> response = restTemplate.exchange(BASE_URI, HttpMethod.GET, new HttpEntity<>(headers), PaginatedResponse.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getContent().size());
        assertEquals(0, response.getBody().getPage());
    }

    @Test
    void shouldFindRideWithValidId() {
        // Arrange
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_PASSENGER);
        String VALID_URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(VALID_URI, HttpMethod.GET, new HttpEntity<>(headers), RideResponse.class);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_PASSENGER);
        String INVALID_URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");

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
        String username = "mock-username";
        RideRequest request = TestDataGenerator.randomRideRequest();
        HttpHeaders headers = createHeadersWithTokenAndUsername(username, ROLE_PASSENGER);
        PassengerResponse passengerResponse = TestDataGenerator.randomResponse();
        UUID id = passengerResponse.id();
        BalanceResponse<UUID> balanceResponse = new BalanceResponse<>(id, BigDecimal.valueOf(Integer.MAX_VALUE));
        Mockito.when(passengerClient.getPassengerByUsername(username)).thenReturn(passengerResponse);
        Mockito.when(paymentClient.getBalance(id)).thenReturn(balanceResponse);

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(BASE_URI, HttpMethod.POST, new HttpEntity<>(request, headers), RideResponse.class);

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
        RideRequest request = new RideRequest(null, null, null, null);
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_PASSENGER);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("admin", ROLE_ADMIN);

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, new HttpEntity<>(request, headers), RideResponse.class);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("admin", ROLE_ADMIN);
        String URI = "%s/%s".formatted(BASE_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_DRIVER);
        ResponseEntity<RideResponse> response = restTemplate.exchange(ACTIVATE_URI, HttpMethod.PATCH, new HttpEntity<>(headers), RideResponse.class);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_DRIVER);
        Mockito.doNothing().when(paymentClient).processTransaction(any(TransactionRequest.class));
        Mockito.doNothing().when(driverClient).markDriverAvailable(any(UUID.class));

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(DONE_URI, HttpMethod.PATCH, new HttpEntity<>(headers), RideResponse.class);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_ADMIN);
        ResponseEntity<RideResponse> response = restTemplate.exchange(CANCEL_URI, HttpMethod.PATCH, new HttpEntity<>(headers), RideResponse.class);

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
        HttpHeaders headers = createHeadersWithTokenAndUsername("username", ROLE_PASSENGER);
        RideCompletionRequest request = new RideCompletionRequest(6);

        // Act
        ResponseEntity<RideResponse> response = restTemplate.exchange(RATE_URI, HttpMethod.PATCH, new HttpEntity<>(request, headers), RideResponse.class);

        // Assert
        assertNotNull(response);
        assertAll(
            () -> assertNotNull(response.getBody()),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(6, requireNonNull(response.getBody()).rating())
        );
    }

}
