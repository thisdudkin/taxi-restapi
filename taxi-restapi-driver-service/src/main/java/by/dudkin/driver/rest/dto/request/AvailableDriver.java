package by.dudkin.driver.rest.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record AvailableDriver(Long rideId, Long driverId, Long carId, BigDecimal lat, BigDecimal lng) implements Serializable {
}
