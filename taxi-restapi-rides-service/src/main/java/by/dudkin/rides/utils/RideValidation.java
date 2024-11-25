package by.dudkin.rides.utils;

import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.ErrorMessages;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * @author Alexander Dudkin
 */
@Component
public class RideValidation {

    private static final Map<RideStatus, Set<RideStatus>> allowedTransitionsMap = Map.of(
        RideStatus.PENDING, Set.of(RideStatus.ACTIVE, RideStatus.CANCEL),
        RideStatus.ACTIVE, Set.of(RideStatus.DONE, RideStatus.CANCEL),
        RideStatus.DONE, Collections.emptySet(),
        RideStatus.CANCEL, Collections.emptySet()
    );

    public void validateStatusTransition(RideStatus current, RideStatus newStatus) throws IllegalStateException {
        Set<RideStatus> allowedStatuses = allowedTransitionsMap.getOrDefault(current, Collections.emptySet());
        if (!allowedStatuses.contains(newStatus)) {
            throw new IllegalStateException(ErrorMessages.INVALID_STATUS_TRANSITION);
        }
    }

}
