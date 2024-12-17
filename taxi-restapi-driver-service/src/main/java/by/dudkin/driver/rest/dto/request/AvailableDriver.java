package by.dudkin.driver.rest.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AvailableDriver(UUID rideId, UUID driverId, UUID carId, BigDecimal lat, BigDecimal lng) implements Serializable {
}
