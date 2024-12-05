package by.dudkin.rides.service;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.repository.RideRepository;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.service.api.RideService;
import by.dudkin.rides.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Sql("classpath:data.sql")
@ActiveProfiles({"test", "kafka"})
@EmbeddedKafka(partitions = 1, topics = {"ride-requests"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RideServiceImplCTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    RideService rideService;

    @Autowired
    RideRepository rideRepository;

    @Test
    void shouldFindRide() {
        // Act
        RideResponse response = rideService.read(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void shouldCreateRide() {
        // Arrange
        RideRequest request = TestDataGenerator.randomRideRequest();

        // Act
        RideResponse response = rideService.create(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.price());
        assertEquals(request.passengerId(), response.passengerId());
    }

    @Test
    void shouldUpdateRide() {
        // Arrange
        RideRequest request = TestDataGenerator.randomRideRequest();

        // Act
        RideResponse response = rideService.update(5L, request);

        // Assert
        assertNotNull(response);
        assertEquals(request.from(), response.from());
        assertEquals(request.passengerId(), response.passengerId());
    }

    @Test
    void shouldDeleteRide() {
        // Act
        rideService.delete(6L);

        // Assert
        var response = rideRepository.findById(6L);
        assertThat(response).isEmpty();
    }

    @Test
    void shouldReturnPaginatedResponse() {
        // Arrange
        var pageable = PageRequest.of(0, 2);

        // Act
        var response = rideService.readAll(null, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
    }


}
