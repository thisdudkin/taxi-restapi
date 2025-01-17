package by.dudkin.rides.utils;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.rides.rest.advice.custom.EntityValidationConflictException;
import by.dudkin.rides.rest.advice.custom.IllegalStatusTransitionException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@Component
public class RideStatusTransitionValidator implements Validator {

    private static final Map<RideStatus, Set<RideStatus>> transitionMap = Map.of(
        RideStatus.PENDING, Set.of(RideStatus.ASSIGNED, RideStatus.CANCEL),
        RideStatus.ASSIGNED, Set.of(RideStatus.ACTIVE, RideStatus.CANCEL),
        RideStatus.ACTIVE, Set.of(RideStatus.DONE, RideStatus.CANCEL),
        RideStatus.DONE, Collections.emptySet(),
        RideStatus.CANCEL, Collections.emptySet()
    );

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RideStatusTransition.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (!(target instanceof RideStatusTransition(RideStatus current, RideStatus next))) {
            throw new IllegalStatusTransitionException(ErrorMessages.VALIDATION_FAILED);
        }

        Set<RideStatus> allowedStatuses = transitionMap.get(current);
        if (allowedStatuses == null || !allowedStatuses.contains(next)) {
            throw new EntityValidationConflictException(ErrorMessages.INVALID_TRANSITION);
        }
    }
}
