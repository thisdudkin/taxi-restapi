package by.dudkin.driver.rest.dto.response;

import by.dudkin.common.enums.AssignmentStatus;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record AssignmentResponse(
        @NotNull Long id,
        @NotNull DriverResponse driver,
        @NotNull CarResponse car,
        @NotNull Instant assignmentDate,
        @NotNull AssignmentStatus status,
        Instant createdAt,
        Instant updatedAt
) implements Serializable {}