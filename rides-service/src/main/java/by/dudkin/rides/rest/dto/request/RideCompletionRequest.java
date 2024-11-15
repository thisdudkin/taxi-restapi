package by.dudkin.rides.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RideCompletionRequest(@Min(1) @NotNull Integer rating) implements Serializable { }
