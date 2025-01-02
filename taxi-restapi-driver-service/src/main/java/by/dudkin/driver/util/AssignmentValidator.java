package by.dudkin.driver.util;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentValidator {

    private final AssignmentRepository assignmentRepository;

    public void validateCarAvailability(UUID carId) {
        assignmentRepository.findActiveAssignmentByCarId(carId)
            .ifPresent(assignment -> {
                throw new IllegalStateException(ErrorMessages.CAR_ALREADY_BOOKED);
            });
    }

    public void validateCarAvailability(String licensePlate) {
        assignmentRepository.findActiveAssignmentByLicencePlate(licensePlate)
            .ifPresent(assignment -> {
                throw new IllegalStateException(ErrorMessages.CAR_ALREADY_BOOKED);
            });
    }

    public void validateStatus(UUID assignmentId) {
        assignmentRepository.findById(assignmentId)
            .filter(assignment -> assignment.getStatus() == AssignmentStatus.COMPLETED)
            .ifPresent(assignment -> {
                throw new IllegalStateException(ErrorMessages.ASSIGNMENT_ALREADY_COMPLETED);
            });
    }
}
