package by.dudkin.driver.rest.dto.request;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RideAssignment(Long rideId, Long driverId, Long carId) implements Serializable {
}
