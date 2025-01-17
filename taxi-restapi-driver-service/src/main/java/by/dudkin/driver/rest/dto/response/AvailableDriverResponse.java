package by.dudkin.driver.rest.dto.response;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AvailableDriverResponse(UUID driverId, UUID carId) implements Serializable {}
