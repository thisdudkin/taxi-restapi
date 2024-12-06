package by.dudkin.notification.domain;

import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record RideRequest(Long id, Long rideId, BigDecimal lat, BigDecimal lng, BigDecimal price) {
}
