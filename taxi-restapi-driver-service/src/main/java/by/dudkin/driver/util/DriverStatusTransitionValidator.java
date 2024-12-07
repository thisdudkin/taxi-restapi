package by.dudkin.driver.util;

import by.dudkin.common.enums.DriverStatus;
import by.dudkin.common.util.ErrorMessages;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;
import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@Component
public class DriverStatusTransitionValidator implements Validator {

    private final Map<DriverStatus, Set<DriverStatus>> transitionMap = Map.of(
        DriverStatus.READY, Set.of(DriverStatus.ON_TRIP, DriverStatus.OFFLINE),
        DriverStatus.ON_TRIP, Set.of(DriverStatus.READY),
        DriverStatus.OFFLINE, Set.of(DriverStatus.READY)
    );

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DriverStatusTransition.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (!(target instanceof DriverStatusTransition(DriverStatus current, DriverStatus next))) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_FAILED);
        }

        Set<DriverStatus> allowedStatuses = transitionMap.get(current);
        if (allowedStatuses == null || !allowedStatuses.contains(next)) {
            throw new IllegalStateException(ErrorMessages.INVALID_TRANSITION);
        }
    }

}
