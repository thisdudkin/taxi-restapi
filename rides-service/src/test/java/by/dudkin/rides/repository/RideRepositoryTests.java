package by.dudkin.rides.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Dudkin
 */
@DataJpaTest
@Testcontainers
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RideRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    RideRepository rideRepository;

    @Test
    void testFindAllByPassengerIdWithValidID() {
        // Arrange
        long passengerId = 1L;

        // Act
        var rides = rideRepository.findAllByPassengerId(passengerId);

        // Assert
        assertThat(rides).isNotNull();
        assertThat(rides).hasSize(1);
    }

    @Test
    void testFindAllByPassengerIdWithInvalidID() {
        // Arrange
        long invalid = 999L;

        // Act
        var rides = rideRepository.findAllByPassengerId(invalid);

        // Assert
        assertThat(rides).isEqualTo(Collections.emptyList());
    }

    @Test
    void testFindAllByDriverIdWithValidID() {
        // Arrange
        long driverId = 101L;

        // Act
        var rides = rideRepository.findAllByDriverId(driverId);

        // Assert
        assertThat(rides).isNotNull();
        assertThat(rides).hasSize(1);
    }

    @Test
    void testFindAllByDriverIdWithInvalidID() {
        // Arrange
        long invalid = 999L;

        // Act
        var rides = rideRepository.findAllByDriverId(invalid);

        // Assert
        assertThat(rides).isEqualTo(Collections.emptyList());
    }

    @Test
    void testFindAllByCarIdWithValidID() {
        // Arrange
        long carId = 1004L;

        // Act
        var rides = rideRepository.findAllByCarId(carId);

        // Assert
        assertThat(rides).isNotNull();
        assertThat(rides).hasSize(1);
    }

    @Test
    void testFindAllByCarIdWithInvalidID() {
        // Arrange
        long invalid = 999L;

        // Act
        var rides = rideRepository.findAllByCarId(invalid);

        // Assert
        assertThat(rides).isEqualTo(Collections.emptyList());
    }

}
