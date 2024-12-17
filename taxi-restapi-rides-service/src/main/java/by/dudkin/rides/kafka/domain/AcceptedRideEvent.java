package by.dudkin.rides.kafka.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AcceptedRideEvent(UUID rideId, UUID driverId, UUID carId) implements Serializable {}
