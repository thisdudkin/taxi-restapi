package by.dudkin.driver.rest.dto.response;

import by.dudkin.common.enums.AssignmentStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AssignmentResponse(
        UUID id,
        DriverResponse driver,
        CarResponse car,
        Instant assignmentDate,
        AssignmentStatus status,
        Instant createdAt,
        Instant updatedAt
) implements Serializable {}
