package by.dudkin.notification.domain;

import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record AvailableDriver(Long driverId, Long carId, BigDecimal lat, BigDecimal lng) {
}
