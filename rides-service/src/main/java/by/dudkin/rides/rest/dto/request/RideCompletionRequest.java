package by.dudkin.rides.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RideCompletionRequest(@NotNull @Positive Long rideId,
                                    @Min(1) @NotNull Integer rating
) implements Serializable { }
