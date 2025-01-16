package by.dudkin.passenger.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.passenger.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Dudkin
 */
class PassengerServiceImplUTests {

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;
    private PassengerRequest passengerRequest;
    private PassengerResponse passengerResponse;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        passenger = TestDataGenerator.randomPassenger();
        passengerRequest = TestDataGenerator.randomRequest();
        passengerResponse = TestDataGenerator.randomResponse();
    }

    @Test
    void whenFindAll_thenReturnPaginatedResponse() {
        // Arrange
        var pageable = Pageable.unpaged();
        var passengers = List.of(passenger);
        var passengerPage = new PageImpl<>(passengers);
        when(passengerRepository.findAll(pageable)).thenReturn(passengerPage);
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        // Act
        var response = passengerService.findAll(pageable);

        // Assert
        assertNotNull(response);
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst()).isEqualTo(passengerResponse);
    }

    @Test
    void whenFindById_thenReturnPassengerResponse() {
        // Arrange
        UUID passengerId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(passenger));
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        // Act
        PassengerResponse response = passengerService.findById(passengerId);

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(passengerResponse);
    }

    @Test
    void whenFindByInvalidId_thenThrowPassengerNotFoundException() {
        // Arrange
        UUID invalidId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        when(passengerRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> passengerService.findById(invalidId))
            .isInstanceOf(PassengerNotFoundException.class)
            .hasMessage(ErrorMessages.PASSENGER_NOT_FOUND);
    }

    @Test
    void whenCreatePassenger_thenReturnResponse() {
        // Arrange
        when(passengerMapper.toPassenger(passengerRequest)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        // Act
        PassengerResponse response = passengerService.create(passengerRequest, "x-username");

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(passengerResponse);
    }

    @Test
    void whenUpdatePassenger_thenReturnUpdatedResponse() {
        // Arrange
        UUID passengerId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        var updatedRequest = TestDataGenerator.randomRequest();
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(passenger));
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        // Act
        PassengerResponse response = passengerService.update(passengerId, updatedRequest);

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(passengerResponse);
    }

    @Test
    void whenDeletePassenger_thenNoException() {
        // Arrange
        UUID passengerId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(passenger));

        // Act & Assert
        assertThatCode(() -> passengerService.delete(passengerId)).doesNotThrowAnyException();
        verify(passengerRepository, times(1)).delete(passenger);
    }

    @Test
    void whenGetOrUpdate_thenReturnPassenger() {
        // Arrange
        UUID passengerId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(passenger));

        // Act
        Passenger response = passengerService.getOrThrow(passengerId);

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(passenger);
    }

    @Test
    void whenGetOrUpdateWithInvalidId_thenThrowException() {
        // Arrange
        UUID invalidId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        when(passengerRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> passengerService.getOrThrow(invalidId))
            .isInstanceOf(PassengerNotFoundException.class)
            .hasMessage(ErrorMessages.PASSENGER_NOT_FOUND);
    }

}
