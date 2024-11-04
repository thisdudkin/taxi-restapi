package by.dudkin.driver.rest.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record AssignmentRequest(
        long driverId,
        long carId,
        @NotNull Instant assignmentDate
) implements Serializable {}
