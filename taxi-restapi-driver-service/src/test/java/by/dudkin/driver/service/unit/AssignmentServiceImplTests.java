package by.dudkin.driver.service.unit;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.Assignment;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.mapper.AssignmentMapper;
import by.dudkin.driver.repository.AssignmentRepository;
import by.dudkin.driver.rest.advice.custom.AssignmentNotFoundException;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.service.AssignmentServiceImpl;
import by.dudkin.driver.service.api.CarService;
import by.dudkin.driver.service.api.DriverService;
import by.dudkin.driver.util.AssignmentValidator;
import by.dudkin.driver.util.TestDataGenerator;
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
class AssignmentServiceImplTests {

    @Mock
    private AssignmentMapper assignmentMapper;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private CarService carService;

    @Mock
    private AssignmentValidator assignmentValidator;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    private Assignment assignment;
    private AssignmentRequest assignmentRequest;
    private AssignmentResponse assignmentResponse;
    private Driver driver;
    private Car car;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        driver = TestDataGenerator.randomDriver();
        car = TestDataGenerator.randomCar();
        assignment = TestDataGenerator.randomAssignment(driver, car);
        assignmentRequest = TestDataGenerator.randomAssignmentRequest();
        assignmentResponse = TestDataGenerator.randomAssignmentResponse();
    }

    @Test
    void whenCreateAssignment_thenReturnAssignmentResponse() {
        // Arrange
        when(carService.getOrThrow(assignmentRequest.licencePlate())).thenReturn(car);
        when(driverService.getOrThrow(UUID.randomUUID())).thenReturn(driver);
        when(assignmentMapper.toAssignment(assignmentRequest)).thenReturn(assignment);
        when(assignmentRepository.save(assignment)).thenReturn(assignment);
        when(assignmentMapper.toResponse(assignment)).thenReturn(assignmentResponse);

        // Act
        var response = assignmentService.create(assignmentRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(assignmentResponse);
    }

    @Test
    void whenFindById_thenReturnAssignmentResponse() {
        // Arrange
        UUID assignmentId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(assignmentRepository.findWithDriverAndCarById(assignmentId)).thenReturn(Optional.ofNullable(assignment));
        when(assignmentMapper.toResponse(assignment)).thenReturn(assignmentResponse);

        // Act
        var response = assignmentService.findById(assignmentId);

        // Assert
        assertThat(response).isEqualTo(assignmentResponse);
    }

    @Test
    void whenFindByIdAndAssignmentNotFound_thenThrowAssignmentNotFoundException() {
        // Arrange
        UUID undefinedId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        when(assignmentRepository.findWithDriverAndCarById(undefinedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> assignmentService.findById(undefinedId))
                .isInstanceOf(AssignmentNotFoundException.class)
                .hasMessage(ErrorMessages.ASSIGNMENT_NOT_FOUND);
    }

    @Test
    void whenFindAll_thenReturnPaginatedResponse() {
        // Arrange
        var pageable = Pageable.unpaged();
        var assignments = List.of(assignment);
        var assignmentPage = new PageImpl<>(assignments);
        when(assignmentRepository.findAll((Specification<Assignment>) null, pageable)).thenReturn(assignmentPage);
        when(assignmentMapper.toResponse(assignment)).thenReturn(assignmentResponse);

        // Act
        var response = assignmentService.findAll(null, pageable);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst()).isEqualTo(assignmentResponse);
    }

    @Test
    void whenDeleteAssignment_thenNoException() {
        // Arrange
        UUID assignmentId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(assignmentRepository.findWithDriverAndCarById(assignmentId)).thenReturn(Optional.ofNullable(assignment));

        // Act & Assert
        assertThatCode(() -> assignmentService.delete(assignmentId)).doesNotThrowAnyException();
        verify(assignmentRepository, times(1)).delete(assignment);
    }

    @Test
    void whenGetOrThrow_thenReturnAssignment() {
        // Arrange
        UUID assignmentId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6981");
        when(assignmentRepository.findWithDriverAndCarById(assignmentId)).thenReturn(Optional.ofNullable(assignment));

        // Act
        var result = assignmentService.getOrThrow(assignmentId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(assignment);
    }

    @Test
    void whenGetOrThrowAndAssignmentNotFound_thenThrowAssignmentNotFoundException() {
        // Arrange
        UUID undefinedId = UUID.fromString("862eb8bc-8d7e-4a44-9dd2-cc258faf6911");
        when(assignmentRepository.findWithDriverAndCarById(undefinedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> assignmentService.getOrThrow(undefinedId))
                .isInstanceOf(AssignmentNotFoundException.class)
                .hasMessage(ErrorMessages.ASSIGNMENT_NOT_FOUND);
    }

}
