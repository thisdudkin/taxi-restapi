package by.dudkin.passenger.service;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.passenger.util.TestDataGenerator;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("test")
@Sql("classpath:data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PassengerServiceCTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    PassengerService passengerService;

    @Autowired
    PassengerRepository passengerRepository;

    @Test
    void shouldFindById() {
        // Act
        var response = passengerService.findById(1L);

        // Assert
        assertNotNull(response);
        assertThat(response.info()).isNotNull();
    }

    @Test
    void shouldCreatePassenger() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();

        // Act
        PassengerResponse response = passengerService.create(request);

        // Assert
        assertNotNull(response);
        assertThat(response.info().getFirstName()).isEqualTo(request.info().getFirstName());
    }

    @Test
    void shouldUpdatePassenger() {
        // Arrange
        PassengerRequest request = TestDataGenerator.randomRequest();

        // Act
        PassengerResponse response = passengerService.update(2L, request);

        // Assert
        assertNotNull(response);
        assertThat(response.info().getFirstName()).isEqualTo(request.info().getFirstName());
    }

    @Test
    void shouldDeletePassenger() {
        // Act
        passengerService.delete(3L);

        // Assert
        Passenger response = passengerRepository.findById(3L).orElse(null);
        assertNull(response);
    }

    @Test
    void shouldReturnPaginatedRequest() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 3);

        // Act
        PaginatedResponse<PassengerResponse> response = passengerService.findAll(pageable);

        // Assert
        assertNotNull(response);
        assertThat(response.getContent()).hasSize(3);
    }

}
