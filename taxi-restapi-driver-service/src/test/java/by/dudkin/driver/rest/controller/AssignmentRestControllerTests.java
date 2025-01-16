package by.dudkin.driver.rest.controller;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
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

import java.util.UUID;

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
    void shouldReturn401HttpStatusCode() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, null, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
    }

    @Test
    void shouldReturn403HttpStatusCode() {
        // Arrange
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_PASSENGER);
        HttpEntity<AssignmentRequest> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.GET, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ProblemDetail.class);
    }

    @Test
    void shouldFindAllAssignments() {
        // Arrange
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<PaginatedResponse> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.GET, entity, PaginatedResponse.class);

        // Assert
        assertThat(response).isNotNull();
        assertNotNull(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent().size()).isGreaterThan(2);
        assertThat(response.getBody().getContent().size()).isLessThan(999);
    }

    @Test
    void shouldFindAssignmentWhenValidId() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(URI, HttpMethod.GET, entity, AssignmentResponse.class);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().car().id()).isNotNull();
        assertThat(response.getBody().driver().id()).isNotNull();
    }

    @Test
    void shouldNotFindAssignmentWhenInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<?> response = restTemplate.exchange(URI, HttpMethod.GET, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateAssignment() {
        // Arrange
        String licencePlate = "GJW235";
        var request = TestDataGenerator.randomAssignmentRequestWithLicensePlate(licencePlate);
        HttpHeaders headers = TestJwtUtils.createHeadersWithTokenAndUsername("username4", TestJwtUtils.ROLE_ADMIN);
        HttpEntity<AssignmentRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.POST, entity, AssignmentResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().driver().id()).isNotNull();
        assertThat(response.getBody().car().id()).isNotNull();
    }

    @Test
    void shouldNotCreateAssignmentWhenValidationFails() {
        // Arrange
        String licencePlate = "LICENSE";
        var invalid = new AssignmentRequest(licencePlate, null);
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<AssignmentRequest> entity = new HttpEntity<>(invalid, headers);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.POST, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCancelAssignment() {
        // Arrange
        var URI = "%s/%s".formatted(ASSIGNMENTS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, entity, AssignmentResponse.class);

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
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, entity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
