package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
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
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
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
    @SuppressWarnings("unchecked")
    void shouldFindAllDrivers() {
        // Act
        PaginatedResponse<DriverResponse> drivers = restTemplate.getForObject(DRIVERS_URI, PaginatedResponse.class);

        // Assert
        assertThat(drivers).isNotNull();
        assertThat(drivers.getContent().size()).isGreaterThan(3);
        assertThat(drivers.getContent().size()).isLessThan(999);
    }

    @Test
    void shouldFindDriverWhenValidID() {
        // Arrange
        var URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6983");

        // Act
        ResponseEntity<DriverResponse> response = restTemplate.exchange(URI, HttpMethod.GET, null, DriverResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().info().getFirstName()).isEqualTo(ALICE_FIRSTNAME);
    }

    @Test
    void shouldNotFindDriverWhenInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(DRIVERS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateDriver() {
        // Arrange
        var request = TestDataGenerator.randomDriverRequest();

        // Act
        ResponseEntity<DriverResponse> response = restTemplate.exchange(DRIVERS_URI, HttpMethod.POST, new HttpEntity<>(request), DriverResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().info().getPhone()).isNotEmpty();
    }

    @Test
    void shouldNotCreateDriverWhenValidationFails() {
        // Arrange
        var invalid = new DriverRequest(null, null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(DRIVERS_URI, HttpMethod.POST, new HttpEntity<>(invalid, headers), ProblemDetail.class);

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

        // Act
        ResponseEntity<DriverResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, new HttpEntity<>(request), DriverResponse.class);

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

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
