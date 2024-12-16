package by.dudkin.payment.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Transaction(UUID id, UUID driverId, UUID passengerId, BigDecimal amount, LocalDateTime createdAt, UUID rideId) implements Serializable {

    public Transaction(UUID id, UUID driverId, UUID passengerId, BigDecimal amount, UUID rideId) {
        this(id, driverId, passengerId, amount, LocalDateTime.now(), rideId);
    }

}
