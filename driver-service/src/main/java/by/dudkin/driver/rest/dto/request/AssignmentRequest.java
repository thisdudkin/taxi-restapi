package by.dudkin.driver.rest.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record AssignmentRequest(
        // TODO: Delete driverId in future. It will be set from Authorization header
        @NotNull Long driverId,
        @NotNull Long carId,
        @NotNull Instant assignmentDate
) implements Serializable {}
