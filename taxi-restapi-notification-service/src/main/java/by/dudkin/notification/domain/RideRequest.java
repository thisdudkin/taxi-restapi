package by.dudkin.notification.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record RideRequest(UUID id, UUID rideId, BigDecimal lat, BigDecimal lng, BigDecimal price) {
}
