package by.dudkin.driver.service.component;

import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.service.api.CarService;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarServiceComponentTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

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
        var response = carService.findById(100L);

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
        var response = carService.update(104L, updatedRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.color()).isEqualTo(COLOR_WHITE);
    }

    @Test
    @Sql("classpath:data.sql")
    void shouldDeleteCar() {
        // Act
        carService.delete(104L);

        // Assert
        var deletedCar = carRepository.findById(104L).orElse(null);
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
