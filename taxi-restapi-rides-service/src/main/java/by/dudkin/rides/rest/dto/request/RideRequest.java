package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.util.Location;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link by.dudkin.rides.domain.Ride}
 */
public record RideRequest(@NotNull Location from,
                          @NotNull Location to,
                          @NotNull PaymentMethod paymentMethod,
                          @Nullable String promocode
) implements Serializable { }
