package by.dudkin.driver.service.unit;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.mapper.DriverMapper;
import by.dudkin.driver.repository.DriverRepository;
import by.dudkin.driver.rest.advice.custom.DriverNotFoundException;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import by.dudkin.driver.service.DriverServiceImpl;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alexander Dudkin
 */
class DriverServiceImplTests {

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private DriverRequest driverRequest;
    private DriverResponse driverResponse;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        driver = TestDataGenerator.randomDriver();
        driverRequest = TestDataGenerator.randomDriverRequest();
        driverResponse = TestDataGenerator.randomDriverResponse();
    }

    @Test
    void whenFindAll_thenReturnPaginatedResponse() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        List<Driver> drivers = List.of(driver);
        PageImpl<Driver> driverPage = new PageImpl<>(drivers);
        when(driverRepository.findAll(pageable)).thenReturn(driverPage);
        when(driverMapper.toResponse(driver)).thenReturn(driverResponse);

        // Act
        PaginatedResponse<DriverResponse> response = driverService.findAll(pageable);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst()).isEqualTo(driverResponse);
    }

    @Test
    void whenFindById_thenReturnDriverResponse() {
        // Arrange
        var driverId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(driverRepository.findWithAssignmentsAndCarsById(driverId)).thenReturn(Optional.ofNullable(driver));
        when(driverMapper.toResponse(driver)).thenReturn(driverResponse);

        // Act
        DriverResponse response = driverService.findById(driverId);

        // Assert
        assertThat(response).isEqualTo(driverResponse);
    }

    @Test
    void whenFindByIdAndDriverNotFound_thenThrowDriverNotFoundException() {
        // Arrange
        var undefinedId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        when(driverRepository.findWithAssignmentsAndCarsById(undefinedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> driverService.findById(undefinedId))
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessage(ErrorMessages.DRIVER_NOT_FOUND);
    }

    @Test
    void whenCreateDriver_thenReturnDriverResponse() {
        // Arrange
        when(driverMapper.toDriver(driverRequest)).thenReturn(driver);
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toResponse(driver)).thenReturn(driverResponse);

        // Act
        DriverResponse response = driverService.create(driverRequest, "username");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(driverResponse);
    }

    @Test
    void whenDeleteDriver_thenNoException() {
        // Arrange
        var driverId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(driverRepository.findWithAssignmentsAndCarsById(driverId)).thenReturn(Optional.ofNullable(driver));

        // Act & Assert
        assertThatCode(() -> driverService.delete(driverId)).doesNotThrowAnyException();
        verify(driverRepository, times(1)).delete(driver);
    }

    @Test
    void whenGetOrThrow_thenReturnDriver() {
        // Arrange
        var driverId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(driverRepository.findWithAssignmentsAndCarsById(driverId)).thenReturn(Optional.ofNullable(driver));

        // Act
        Driver result = driverService.getOrThrow(driverId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(driver);
    }

    @Test
    void whenGetOrThrowAndDriverNotFound_thenThrowDriverNotFoundException() {
        // Arrange
        var undefinedId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        when(driverRepository.findWithAssignmentsAndCarsById(undefinedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> driverService.getOrThrow(undefinedId))
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessage(ErrorMessages.DRIVER_NOT_FOUND);
    }

}
