package by.dudkin.notification.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AvailableDriver(UUID driverId, UUID carId, BigDecimal lat, BigDecimal lng) {
}
