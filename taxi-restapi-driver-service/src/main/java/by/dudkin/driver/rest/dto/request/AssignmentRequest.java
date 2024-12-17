package by.dudkin.driver.rest.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AssignmentRequest(
        UUID driverId,
        UUID carId,
        @NotNull
        Instant assignmentDate
) implements Serializable {
}
