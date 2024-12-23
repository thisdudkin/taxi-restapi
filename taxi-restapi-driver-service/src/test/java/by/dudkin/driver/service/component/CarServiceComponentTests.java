package by.dudkin.driver.service.component;

import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.repository.DriverLocationRepository;
import by.dudkin.driver.service.api.CarService;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"available-drivers", "ride-requests"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarServiceComponentTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @MockBean
    DriverLocationRepository driverLocationRepository;

    @MockBean
    SecurityFilterChain jwtFilterChain;

    @Autowired
    CarService carService;

    @Autowired
    CarRepository carRepository;

    private static final String COLOR_BLUE = "Blue";
    private static final String COLOR_WHITE = "White";
    private static final String TOYOTA_PRIUS = "Toyota Prius";

    @Test
    @Sql("classpath:data.sql")
    void shouldFindCarById() {
        // Act
        var response = carService.findById(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981"));

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.color()).isEqualTo(COLOR_BLUE);
        assertThat(response.model()).isEqualTo(TOYOTA_PRIUS);
    }

    @Test
    void shouldCreateCar() {
        // Arrange
        var carRequest = TestDataGenerator.randomCarRequest();

        // Act
        var carResponse = carService.create(carRequest);

        // Assert
        assertThat(carResponse).isNotNull();
        assertThat(carResponse.model()).isNotEmpty();
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldUpdateCar() {
        // Arrange
        var updatedRequest = TestDataGenerator.randomCarRequestWithColor(COLOR_WHITE);

        // Act
        var response = carService.update(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6984"), updatedRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.color()).isEqualTo(COLOR_WHITE);
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldDeleteCar() {
        // Act
        carService.delete(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6984"));

        // Assert
        var deletedCar = carRepository.findById(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6984")).orElse(null);
        assertThat(deletedCar).isNull();
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldReturnPaginatedResponse() {
        // Arrange
        var pageable = PageRequest.of(0, 2);

        // Act
        var paginatedResponse = carService.findAll(pageable);

        // Assert
        assertThat(paginatedResponse).isNotNull();
        assertThat(paginatedResponse.getContent()).hasSize(2);
    }

}
