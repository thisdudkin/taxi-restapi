package by.dudkin.driver.rest.dto.request;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record RideAssignment(UUID rideId, UUID driverId, UUID carId) implements Serializable {
}
