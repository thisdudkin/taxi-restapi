package by.dudkin.rides.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.rides.feign.PassengerClient;
import by.dudkin.rides.repository.RideRepository;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.PassengerResponse;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.service.api.RideService;
import by.dudkin.rides.util.TestDataGenerator;
import by.dudkin.rides.util.TestJwtUtils;
import by.dudkin.rides.util.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author Alexander Dudkin
 */
@SpringBootTest
@Testcontainers
@Sql("classpath:data.sql")
@ActiveProfiles({"test", "kafka"})
@Import(TestSecurityConfig.class)
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

    @MockBean
    PassengerClient passengerClient;

    @Test
    void shouldFindRide() {
        // Act
        RideResponse response = rideService.read(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981"));

        // Assert
        assertNotNull(response);
        assertNotNull(response.id());
    }

    @Test
    void shouldCreateRide() {
        // Arrange
        TestJwtUtils.setMockAuthentication();
        RideRequest request = TestDataGenerator.randomRideRequest();
        PassengerResponse passengerResponse = TestDataGenerator.randomResponse();
        BalanceResponse<UUID> balanceResponse = new BalanceResponse<>(passengerResponse.id(), BigDecimal.valueOf(Integer.MAX_VALUE));
        Mockito.when(passengerClient.getPassengerByUsername("mock-username")).thenReturn(passengerResponse);
        Mockito.when(passengerClient.checkBalance(passengerResponse.id())).thenReturn(balanceResponse);

        // Act
        RideResponse response = rideService.create(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.price());
        assertThat(response.from().getLat()).isEqualTo(request.from().getLat());
    }

    @Test
    void shouldUpdateRide() {
        // Arrange
        RideRequest request = TestDataGenerator.randomRideRequest();

        // Act
        RideResponse response = rideService.update(UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6985"), request);

        // Assert
        assertNotNull(response);
        assertEquals(request.from(), response.from());
    }

    @Test
    void shouldDeleteRide() {
        // Arrange
        UUID id = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6986");

        // Act
        rideService.delete(id);

        // Assert
        var response = rideRepository.findById(id);
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
