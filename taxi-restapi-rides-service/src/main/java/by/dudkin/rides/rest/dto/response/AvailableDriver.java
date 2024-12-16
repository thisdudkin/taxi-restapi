package by.dudkin.rides.rest.dto.response;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AvailableDriver(UUID driverId, UUID carId) {
}
