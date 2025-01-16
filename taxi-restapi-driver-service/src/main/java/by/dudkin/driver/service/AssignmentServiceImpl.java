package by.dudkin.driver.service;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.Assignment;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.mapper.AssignmentMapper;
import by.dudkin.driver.repository.AssignmentRepository;
import by.dudkin.driver.rest.advice.custom.AssignmentNotFoundException;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.AvailableDriverResponse;
import by.dudkin.driver.service.api.AssignmentService;
import by.dudkin.driver.service.api.CarService;
import by.dudkin.driver.service.api.DriverService;
import by.dudkin.driver.util.AssignmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentMapper assignmentMapper;
    private final AssignmentRepository assignmentRepository;
    private final DriverService driverService;
    private final CarService carService;
    private final AssignmentValidator assignmentValidator;
    private final AvailableDriverService availableDriverService;

    @Override
    public AssignmentResponse create(AssignmentRequest assignmentRequest) {
        assignmentValidator.validateCarAvailability(assignmentRequest.licencePlate());

        Car car = carService.getOrThrow(assignmentRequest.licencePlate());
        Driver driver = driverService.getOrThrow(JwtTokenUtils.getPreferredUsername());

        Assignment assignment = assignmentMapper.toAssignment(assignmentRequest);
        assignment.setDriver(driver);
        assignment.setCar(car);
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse findById(UUID assignmentId) {
        return assignmentMapper.toResponse(getOrThrow(assignmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AssignmentResponse> findAll(Specification<Assignment> spec, Pageable pageable) {
        Page<Assignment> assignmentPage = assignmentRepository.findAll(spec, pageable);
        List<AssignmentResponse> assignmentList = assignmentPage.getContent().stream()
                .map(assignmentMapper::toResponse)
                .toList();

        return PaginatedResponse.fromPage(assignmentPage, assignmentList);
    }

    @Override
    public AssignmentResponse cancelAssignment(UUID assignmentId) {
        assignmentValidator.validateStatus(assignmentId);

        Assignment assignment = getOrThrow(assignmentId);
        assignment.setStatus(AssignmentStatus.COMPLETED);
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public void delete(UUID assignmentId) {
        Assignment assignment = getOrThrow(assignmentId);
        assignmentRepository.delete(assignment);
    }

    @Override
    public AvailableDriverResponse search(String username) {
        AvailableDriverResponse driverByUsername = availableDriverService.findAvailableDriverByUsername(username);
        if (driverByUsername == null) {
            throw new AssignmentNotFoundException(ErrorMessages.ASSIGNMENT_NOT_FOUND);
        }
        return driverByUsername;
    }

    public Assignment getOrThrow(UUID assignmentId) {
        return assignmentRepository.findWithDriverAndCarById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(ErrorMessages.ASSIGNMENT_NOT_FOUND));
    }

}
