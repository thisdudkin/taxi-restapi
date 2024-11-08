package by.dudkin.driver.service.unit;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.mapper.CarMapper;
import by.dudkin.driver.repository.CarRepository;
import by.dudkin.driver.rest.advice.CarNotFoundException;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.service.CarServiceImpl;
import by.dudkin.driver.util.CarValidator;
import by.dudkin.driver.util.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Dudkin
 */
class CarServiceImplTests {

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarValidator carValidator;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;
    private CarRequest carRequest;
    private CarResponse carResponse;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        car = TestDataGenerator.randomCar();
        carRequest = TestDataGenerator.randomCarRequest();
        carResponse = TestDataGenerator.randomCarResponse();
    }

    @Test
    void whenFindAll_thenReturnPaginatedResponse() {
        // Arrange
        var pageable = Pageable.unpaged();
        var cars = List.of(car);
        var carPage = new PageImpl<>(cars);
        when(carRepository.findAll(pageable)).thenReturn(carPage);
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        // Act
        var response = carService.findAll(pageable);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst()).isEqualTo(carResponse);
    }

    @Test
    void whenFindById_thenReturnCarResponse() {
        // Arrange
        long carId = 1L;
        when(carRepository.findWithAssignmentsAndDriversById(carId)).thenReturn(Optional.ofNullable(car));
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        // Act
        var response = carService.findById(carId);

        // Assert
        assertThat(response).isEqualTo(carResponse);
    }

    @Test
    void whenFindByIdAndCarNotFound_thenThrowCarNotFoundException() {
        // Arrange
        long undefinedId = 999L;
        when(carRepository.findWithAssignmentsAndDriversById(undefinedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> carService.findById(undefinedId))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessage(ErrorMessages.CAR_NOT_FOUND);
    }

    @Test
    void whenCreateCar_thenReturnCarResponse() {
        // Arrange
        when(carMapper.toCar(carRequest)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        // Act
        var response = carService.create(carRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(carResponse);
    }

    @Test
    void whenUpdateCar_thenReturnUpdatedCarResponse() {
        // Arrange
        long carId = 1L;
        var updatedCarRequest = TestDataGenerator.randomCarRequest();
        when(carRepository.findWithAssignmentsAndDriversById(carId)).thenReturn(Optional.ofNullable(car));
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        // Act
        var updatedResponse = carService.update(carId, updatedCarRequest);

        // Assert
        assertThat(updatedResponse).isNotNull();
        assertThat(updatedResponse).isEqualTo(carResponse);
    }

    @Test
    void whenDeleteCar_thenNoException() {
        // Arrange
        long carId = 1L;
        when(carRepository.findWithAssignmentsAndDriversById(carId)).thenReturn(Optional.ofNullable(car));

        // Act & Assert
        assertThatCode(() -> carService.delete(carId)).doesNotThrowAnyException();
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void whenGetOrUpdate_thenReturnCar() {
        // Arrange
        long carId = 1L;
        when(carRepository.findWithAssignmentsAndDriversById(carId)).thenReturn(Optional.ofNullable(car));

        // Act
        var result = carService.getOrThrow(carId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(car);
    }

    @Test
    void whenGetOrThrowAndCarNotFound_thenThrowCarNotFoundException() {
        // Arrange
        long undefinedId = 999L;
        when(carRepository.findWithAssignmentsAndDriversById(undefinedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> carService.getOrThrow(undefinedId))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessage(ErrorMessages.CAR_NOT_FOUND);
    }

}
