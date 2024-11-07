package by.dudkin.driver.util;

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
        if (assignmentRepository.findActiveAssignmentByCarId(carId).isPresent()) {
            throw new IllegalStateException(ErrorMessages.CAR_ALREADY_BOOKED);
        }
    }

}
