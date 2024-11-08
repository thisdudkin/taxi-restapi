package by.dudkin.driver.rest.controller;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.rest.dto.response.PaginatedResponse;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssignmentRestControllerTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate restTemplate;

    private static final String ASSIGNMENTS_URI = "/assignments";

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
    void shouldFindAssignmentWhenValidAssignmentID() {
        // Arrange
        var URI = "%s/%d".formatted(ASSIGNMENTS_URI, 100L);

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(URI, HttpMethod.GET, null, AssignmentResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().car().id()).isEqualTo(100L);
        assertThat(response.getBody().driver().id()).isEqualTo(100L);
    }

    @Test
    @Rollback
    void shouldCreateAssignment() {
        // Arrange
        var request = TestDataGenerator.randomAssignmentRequestWithIds(100L, 105L);

        // Act
        ResponseEntity<AssignmentResponse> response = restTemplate.exchange(ASSIGNMENTS_URI, HttpMethod.POST, new HttpEntity<>(request), AssignmentResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().driver().id()).isEqualTo(100L);
        assertThat(response.getBody().car().id()).isEqualTo(105L);
    }

    @Test
    @Rollback
    void shouldCancelAssignment() {
        // Arrange
        var URI = "%s/%d".formatted(ASSIGNMENTS_URI, 101L);

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
        var URI = "%s/%d".formatted(ASSIGNMENTS_URI, 101L);

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
