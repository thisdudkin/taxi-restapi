package by.dudkin.notification.kafka.domain;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AcceptedRide(UUID rideId, UUID driverId, UUID carId) {}
