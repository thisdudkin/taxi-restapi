package by.dudkin.driver.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@DataJpaTest
@Testcontainers
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    private static final String VALID_LICENSE_PLATE = "LMN456";
    private static final String NOT_VALID_LICENSE_PLATE = "UNDEFINED";

    @Autowired
    CarRepository carRepository;

    @Test
    void testExistsByLicensePlate() {
        // Act
        var result = carRepository.existsByLicensePlate(VALID_LICENSE_PLATE);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByLicensePlateFailed() {
        // Act
        var undefined = carRepository.existsByLicensePlate(NOT_VALID_LICENSE_PLATE);

        // Assert
        assertThat(undefined).isFalse();
    }

}
