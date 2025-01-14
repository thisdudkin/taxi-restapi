package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.util.Location;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link by.dudkin.rides.domain.Ride}
 */
public record RideRequest(@NotNull UUID passengerId,
                          @NotNull Location from,
                          @NotNull Location to,
                          @NotNull PaymentMethod paymentMethod,
                          String promocode
) implements Serializable { }
