package by.dudkin.driver.rest.controller;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.service.DriverLocationService;
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
class AssignmentRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @Autowired
    TestRestTemplate restTemplate;

    private static final String ASSIGNMENTS_URI = "/api/assignments";

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllAssignments() {
        // Act
        PaginatedResponse<CarResponse> cars = restTemplate.getForObject(ASSIGNMENTS_URI, PaginatedResponse.class);

        // Assert
        assertThat(cars).isNotNull();
        assertThat(cars.getContent().size()).isGreaterThan(2);
        assertThat(cars.getContent().size()).isLessThan(999);
    }

    @Test
    void shouldFindAssignmentWhenValidId() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(URI, HttpMethod.GET, null, AssignmentResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().car().id()).isNotNull();
        assertThat(response.getBody().driver().id()).isNotNull();
    }

    @Test
    void shouldNotFindAssignmentWhenInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<?> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateAssignment() {
        // Arrange
        var request = TestDataGenerator.randomAssignmentRequestWithIds(
            UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6985"),
            UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6985"));

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.POST, new HttpEntity<>(request), AssignmentResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().driver().id()).isNotNull();
        assertThat(response.getBody().car().id()).isNotNull();
    }

    @Test
    void shouldNotCreateAssignmentWhenValidationFails() {
        // Arrange
        var invalid = new AssignmentRequest(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981"),
            UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981"), null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.POST, new HttpEntity<>(invalid, headers), ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCancelAssignment() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, null, AssignmentResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(AssignmentStatus.COMPLETED);
    }

    @Test
    @Rollback
    void shouldDeleteAssignment() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
