package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.util.Location;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record PendingRide(Long rideId, Location from, Location to, BigDecimal price) implements Serializable { }