package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.util.Location;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link by.dudkin.rides.domain.Ride}
 */
public record RideRequest(@NotNull @Positive Long passengerId,
                          @NotNull Location from,
                          @NotNull Location to,
                          @NotNull PaymentMethod paymentMethod
) implements Serializable { }
