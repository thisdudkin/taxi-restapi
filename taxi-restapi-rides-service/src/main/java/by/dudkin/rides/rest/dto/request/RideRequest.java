package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.rides.domain.Location;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link by.dudkin.rides.domain.Ride}
 */
public record RideRequest(@NotNull @Positive Long passengerId,
                          @NotNull @Positive Long driverId,
                          @NotNull @Positive Long carId,
                          @NotNull Location from,
                          @NotNull Location to,
                          @NotNull PaymentMethod paymentMethod
) implements Serializable { }
