package by.dudkin.rides.rest.dto.response;

import by.dudkin.rides.domain.Location;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record PendingRide(Long rideId, Location from, Location to, BigDecimal price) implements Serializable { }
