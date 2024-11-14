package by.dudkin.driver.service;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.domain.DriverCarAssignment;
import by.dudkin.driver.mapper.AssignmentMapper;
import by.dudkin.driver.repository.AssignmentRepository;
import by.dudkin.driver.rest.advice.custom.AssignmentNotFoundException;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
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

    @Override
    public AssignmentResponse create(AssignmentRequest assignmentRequest) {
        assignmentValidator.validateCarAvailability(assignmentRequest.carId());

        Car car = carService.getOrThrow(assignmentRequest.carId());
        Driver driver = driverService.getOrThrow(assignmentRequest.driverId());

        DriverCarAssignment assignment = assignmentMapper.toAssignment(assignmentRequest);
        assignment.setDriver(driver);
        assignment.setCar(car);
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse findById(long assignmentId) {
        return assignmentMapper.toResponse(getOrThrow(assignmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AssignmentResponse> findAll(Specification<DriverCarAssignment> spec, Pageable pageable) {
        Page<DriverCarAssignment> assignmentPage = assignmentRepository.findAll(spec, pageable);
        List<AssignmentResponse> assignmentList = assignmentPage.getContent().stream()
                .map(assignmentMapper::toResponse)
                .toList();

        return PaginatedResponse.fromPage(assignmentPage, assignmentList);
    }

    @Override
    public AssignmentResponse cancelAssignment(long assignmentId) {
        assignmentValidator.validateStatus(assignmentId);

        DriverCarAssignment assignment = getOrThrow(assignmentId);
        assignment.setStatus(AssignmentStatus.COMPLETED);
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public void delete(long assignmentId) {
        DriverCarAssignment assignment = getOrThrow(assignmentId);
        assignmentRepository.delete(assignment);
    }

    public DriverCarAssignment getOrThrow(long assignmentId) {
        return assignmentRepository.findWithDriverAndCarById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(ErrorMessages.ASSIGNMENT_NOT_FOUND));
    }

}
