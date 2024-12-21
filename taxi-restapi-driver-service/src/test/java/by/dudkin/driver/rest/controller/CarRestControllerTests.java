package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
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
import org.springframework.security.web.SecurityFilterChain;
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
class CarRestControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @MockBean
    SecurityFilterChain jwtFilterChain;

    @Autowired
    TestRestTemplate restTemplate;

    private static final String CARS_URI = "/api/cars";
    private static final String COLOR_BLUE = "Blue";

    @Test
    @SuppressWarnings("unchecked")
    void shouldFindAllCars() {
        // Act
        PaginatedResponse<CarResponse> cars = restTemplate.getForObject(CARS_URI, PaginatedResponse.class);

        // Assert
        assertThat(cars).isNotNull();
        assertThat(cars.getContent().size()).isGreaterThan(2);
        assertThat(cars.getContent().size()).isLessThan(999);
    }

    @Test
    void shouldFindCarWhenValidId() {
        // Arrange
        var URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6981");

        // Act
        ResponseEntity<CarResponse> response = restTemplate.exchange(URI, HttpMethod.GET, null, CarResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().color()).isEqualTo(COLOR_BLUE);
    }

    @Test
    void shouldNotFindCarWhenInvalidId() {
        // Arrange
        var URI = "%s/%s".formatted(CARS_URI, "862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
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
    void shouldCreateCar() {
        // Arrange
        var request = TestDataGenerator.randomCarRequest();

        // Act
        ResponseEntity<CarResponse> response = restTemplate.exchange(CARS_URI, HttpMethod.POST, new HttpEntity<>(request), CarResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().color()).isNotEmpty();
    }

    @Test
    void shouldNotCreateCarWhenValidationFails() {
        // Arrange
        var invalid = new CarRequest("", "", null, 0, "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Act
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(CARS_URI, HttpMethod.POST, new HttpEntity<>(invalid, headers), ProblemDetail.class);

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

        // Act
        ResponseEntity<CarResponse> response = restTemplate.exchange(URI, HttpMethod.PUT, new HttpEntity<>(request), CarResponse.class);

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

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(URI, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

}
