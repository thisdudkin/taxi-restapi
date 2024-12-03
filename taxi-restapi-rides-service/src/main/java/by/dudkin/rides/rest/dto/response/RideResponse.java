package by.dudkin.rides.rest.dto.response;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.Location;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link by.dudkin.rides.domain.Ride}
 */
public record RideResponse(Long id,
                           Long passengerId,
                           Long driverId,
                           Long carId,
                           RideStatus status,
                           Location from,
                           Location to,
                           BigDecimal price,
                           PaymentMethod paymentMethod,
                           LocalDateTime startTime,
                           LocalDateTime endTime,
                           Integer rating,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt
) implements Serializable { }
