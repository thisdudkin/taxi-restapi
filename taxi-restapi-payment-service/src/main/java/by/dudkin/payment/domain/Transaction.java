package by.dudkin.payment.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Alexander Dudkin
 */
public record Transaction(long id, long driverId, long passengerId, BigDecimal amount, LocalDateTime createdAt, long rideId) implements Serializable {

    public Transaction(long id, long driverId, long passengerId, BigDecimal amount, long rideId) {
        this(id, driverId, passengerId, amount, LocalDateTime.now(), rideId);
    }

}
