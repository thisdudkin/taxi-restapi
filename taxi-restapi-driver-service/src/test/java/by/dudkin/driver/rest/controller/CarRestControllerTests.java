package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
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

/**
 * @author Alexander Dudkin
 */
@Testcontainers
@Sql("classpath:data.sql")
@Import(TestSecurityConfig.class)
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"available-drivers", "ride-requests"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @Autowired
    TestRestTemplate restTemplate;

    private static final String CARS_URI = "/api/cars";
    private static final String COLOR_BLUE = "Blue";

    @Test
    void shouldReturn401HttpStatusCode() {
        // Arrange
        String URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, null, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldReturn403HttpStatusCode() {
        // Arrange
        String URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_PASSENGER);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, new HttpEntity<>(headers), ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldFindAllCars() {
        // Arrange
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<PaginatedResponse> response = restTemplate.exchange(CARS_URI, HttpMethod.GET, entity, PaginatedResponse.class);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent().size()).isGreaterThan(2);
        assertThat(response.getBody().getContent().size()).isLessThan(999);
    }

    @Test
    void shouldFindCarWhenValidId() {
        // Arrange
        var URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<CarResponse> response = restTemplate.exchange(URI, HttpMethod.GET, entity, CarResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().color()).isEqualTo(COLOR_BLUE);
    }

    @Test
    void shouldNotFindCarWhenInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(URI, HttpMethod.GET, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateCar() {
        // Arrange
        var request = TestDataGenerator.randomCarRequest();
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<CarRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<CarResponse> response = restTemplate.exchange(CARS_URI, HttpMethod.POST, entity, CarResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().color()).isNotEmpty();
    }

    @Test
    void shouldNotCreateCarWhenValidationFails() {
        // Arrange
        var invalid = new CarRequest("", "", null, 0, "");
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<CarRequest> entity = new HttpEntity<>(invalid, headers);

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(CARS_URI, HttpMethod.POST, entity, ProblemDetail.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldUpdateCar() {
        // Arrange
        var request = TestDataGenerator.randomCarRequestWithColor(COLOR_BLUE);
        var URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6984");
        HttpHeaders headers = createHeadersWithToken(TestJwtUtils.ROLE_ADMIN);
        HttpEntity<CarRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<CarResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, entity, CarResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().color()).isEqualTo(COLOR_BLUE);
    }

    @Test
    @Rollback
    void shouldDeleteCar() {
        // Arrange
        var URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6984");
        HttpEntity<?> entity = new HttpEntity<>(createHeadersWithToken(TestJwtUtils.ROLE_ADMIN));

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, entity, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
