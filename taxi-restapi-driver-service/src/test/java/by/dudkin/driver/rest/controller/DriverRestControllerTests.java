package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import by.dudkin.driver.util.TestDataGenerator;
import by.dudkin.driver.util.TestJwtUtils;
import by.dudkin.driver.util.TestSecurityConfig;
import org.junit.jupiter.api.Test;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static by.dudkin.driver.util.TestJwtUtils.createHeadersWithToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
@Import(TestSecurityConfig.class)
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"available-drivers", "ride-requests"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DriverRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @Autowired
    TestRestTemplate restTemplate;

    private static final String DRIVERS_URI = "/api/drivers";
    private static final String BOB_FIRSTNAME = "Bob";
    private static final String ALICE_FIRSTNAME = "Alice";

    @Test
    void shouldReturn401HttpStatusCode() {
        // Arrange
        String URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6983");

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, null, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldReturn403HttpStatusCode() {
        // Arrange
        String URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6983");
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_PASSENGER);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldFindAllDrivers() {
        // Arrange
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<PaginatedResponse> response = restTemplate.exchange(DRIVERS_URI, HttpMethod.GET, entity, PaginatedResponse.class);

        // Assert
        assertThat(response).isNotNull();
        assertNotNull(response.getBody());
        assertThat(response.getBody().getContent().size()).isGreaterThan(3);
        assertThat(response.getBody().getContent().size()).isLessThan(999);
    }

    @Test
    void shouldFindDriverWhenValidID() {
        // Arrange
        var URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6983");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<DriverResponse> response = restTemplate.exchange(URI, HttpMethod.GET, entity, DriverResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().info().getFirstName()).isEqualTo(ALICE_FIRSTNAME);
    }

    @Test
    void shouldNotFindDriverWhenInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateDriver() {
        // Arrange
        var request = TestDataGenerator.randomDriverRequest();
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<DriverRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<DriverResponse> response = restTemplate.exchange(DRIVERS_URI, HttpMethod.POST, entity, DriverResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().info().getPhone()).isNotEmpty();
    }

    @Test
    void shouldNotCreateDriverWhenValidationFails() {
        // Arrange
        var invalid = new DriverRequest(null, null, null);
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<DriverRequest> entity = new HttpEntity<>(invalid, headers);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(DRIVERS_URI, HttpMethod.POST, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldUpdateDriver() {
        // Arrange
        var request = TestDataGenerator.randomDriverRequestWithFirstname(BOB_FIRSTNAME);
        var URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6982");
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<DriverRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<DriverResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, entity, DriverResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().info().getFirstName()).isEqualTo(BOB_FIRSTNAME);
    }

    @Test
    @Rollback
    void shouldDeleteDriver() {
        // Arrange
        var URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6982");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, entity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
