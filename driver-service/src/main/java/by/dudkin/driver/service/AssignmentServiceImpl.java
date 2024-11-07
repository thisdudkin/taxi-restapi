package by.dudkin.driver.service;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.domain.DriverCarAssignment;
import by.dudkin.driver.mapper.AssignmentMapper;
import by.dudkin.driver.repository.AssignmentRepository;
import by.dudkin.driver.rest.advice.AssignmentNotFoundException;
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
    public Page<AssignmentResponse> findAll(Specification<DriverCarAssignment> spec, Pageable pageable) {
        return assignmentRepository.findAllWithDriverAndCar(spec, pageable).map(assignmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> findAllByDriver(long driverId, Pageable pageable) {
        return assignmentRepository.findByDriverId(driverId, pageable).map(assignmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssignmentResponse> findAllByCar(long carId, Pageable pageable) {
        return assignmentRepository.findByCarId(carId, pageable).map(assignmentMapper::toResponse);
    }

    @Override
    public AssignmentResponse cancelAssignment(long assignmentId, AssignmentRequest assignmentRequest) {
        DriverCarAssignment assignment = getOrThrow(assignmentId);
        assignment.setStatus(AssignmentStatus.COMPLETED);
        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    @Override
    public void delete(long assignmentId) {
        DriverCarAssignment assignment = getOrThrow(assignmentId);
        assignmentRepository.delete(assignment);
    }

    private DriverCarAssignment getOrThrow(long assignmentId) {
        return assignmentRepository.findWithDriverAndCarById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(ErrorMessages.ASSIGNMENT_NOT_FOUND));
    }

}
