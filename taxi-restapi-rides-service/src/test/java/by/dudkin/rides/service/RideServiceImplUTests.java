package by.dudkin.rides.service;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.mapper.RideMapper;
import by.dudkin.rides.repository.RideRepository;
import by.dudkin.rides.rest.advice.custom.RideNotFoundException;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

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
class RideServiceImplUTests {

    @Mock
    private RideMapper rideMapper;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private PriceCalculator priceCalculator;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride ride;
    private RideRequest rideRequest;
    private RideResponse rideResponse;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        ride = TestDataGenerator.randomRide();
        rideRequest = TestDataGenerator.randomRideRequest();
        rideResponse = TestDataGenerator.randomRideResponse();
    }

    @Test
    void whenReadAll_thenReturnPaginatedResponse() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        List<Ride> rides = List.of(ride);
        PageImpl<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAll((Specification<Ride>) null, pageable)).thenReturn(ridePage);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        // Act
        PaginatedResponse<RideResponse> response = rideService.readAll(null, pageable);

        // Assert
        assertNotNull(response);
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst()).isEqualTo(rideResponse);
    }

    @Test
    void whenRead_thenReturnRideResponse() {
        // Arrange
        long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.ofNullable(ride));
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        // Act
        RideResponse response = rideService.read(rideId);

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(rideResponse);
    }

    @Test
    void whenReadWithInvalidId_thenThrowRNFException() {
        // Arrange
        long invalidId = 999L;
        when(rideRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> rideService.read(invalidId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(ErrorMessages.RIDE_NOT_FOUND);
    }

    @Test
    void whenCreateRide_thenReturnRideResponse() {
        // Arrange
        when(rideMapper.toRide(rideRequest)).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        // Act
        RideResponse response = rideService.create(rideRequest);

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(rideResponse);
    }

    @Test
    void whenUpdateRide_thenReturnUpdatedRideResponse() {
        // Arrange
        long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.ofNullable(ride));
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        // Act
        RideResponse response = rideService.update(rideId, rideRequest);

        // Assert
        assertNotNull(response);
        assertThat(response).isEqualTo(rideResponse);

        verify(rideMapper).updateRide(rideRequest, ride);
        verify(rideRepository).save(ride);
    }

    @Test
    void whenDeleteRide_thenNoException() {
        // Arrange
        long rideId = 1L;
        when(rideRepository.findById(rideId)).thenReturn(Optional.ofNullable(ride));

        // Act && Assert
        assertThatCode(() -> rideService.delete(rideId)).doesNotThrowAnyException();
        verify(rideRepository, times(1)).delete(ride);
    }

    @Test
    void whenGetOrThrow_thenReturnRide() {
        // Arrange
        long rideId = 1L;
        when(rideRepository.findById(1L)).thenReturn(Optional.ofNullable(ride));

        // Act
        Ride result = rideService.getOrThrow(rideId);

        // Assert
        assertNotNull(result);
        assertThat(result).isEqualTo(ride);
    }

    @Test
    void whenGetOrThrowWithInvalidId_thenThrowRNFException() {
        // Arrange
        long invalidId = 999L;
        when(rideRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> rideService.getOrThrow(invalidId))
            .isInstanceOf(RideNotFoundException.class)
            .hasMessage(ErrorMessages.RIDE_NOT_FOUND);
    }

}
