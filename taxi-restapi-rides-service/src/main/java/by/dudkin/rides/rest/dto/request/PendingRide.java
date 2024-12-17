package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.util.Location;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record PendingRide(UUID rideId, Location from, Location to, BigDecimal price) implements Serializable { }
