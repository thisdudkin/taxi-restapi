package by.dudkin.driver.util;

import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
public class AssignmentValidator {

    private final AssignmentRepository assignmentRepository;

    public void validateCarAvailability(long carId) {
        assignmentRepository.findActiveAssignmentByCarId(carId)
                .ifPresent(assignment -> {
                    throw new IllegalStateException(ErrorMessages.CAR_ALREADY_BOOKED);
                });
    }

    public void validateStatus(long assignmentId) {
        assignmentRepository.findById(assignmentId)
                .filter(assignment -> assignment.getStatus() == AssignmentStatus.COMPLETED)
                .ifPresent(assignment -> {
                    throw new IllegalStateException(ErrorMessages.ASSIGNMENT_ALREADY_COMPLETED);
                });
    }
}
