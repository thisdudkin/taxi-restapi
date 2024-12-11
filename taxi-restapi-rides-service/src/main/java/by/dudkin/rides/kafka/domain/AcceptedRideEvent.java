package by.dudkin.rides.kafka.domain;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record AcceptedRideEvent(long rideId, long driverId, long carId) implements Serializable {}
