package by.dudkin.driver.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@DataJpaTest
@Testcontainers
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DriverRepositoryTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    DriverRepository driverRepository;

    @Test
    void testFindWithAssignmentsAndCars() {
        // Arrange
        long driverId = 100L;

        // Act
        var driver = driverRepository.findWithAssignmentsAndCarsById(driverId);

        // Assert
        assertThat(driver).isPresent();
        assertThat(driver.get().getExperience()).isEqualTo(5);
    }

    @Test
    void testFindWithAssignmentsAndCarsShouldFailed() {
        // Arrange
        long undefinedId = 999L;

        // Act
        var driver = driverRepository.findWithAssignmentsAndCarsById(undefinedId);

        // Assert
        assertThat(driver).isEmpty();
    }

}
