package by.dudkin.notification.kafka.domain;

/**
 * @author Alexander Dudkin
 */
public record AcceptedRide(long rideId, long driverId, long carId) {}
