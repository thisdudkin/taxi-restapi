package by.dudkin.driver.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record AssignmentRequest(
        @NotEmpty String licencePlate,
        @NotNull Instant assignmentDate
) implements Serializable {}
