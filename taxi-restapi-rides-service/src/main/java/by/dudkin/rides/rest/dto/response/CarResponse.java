package by.dudkin.rides.rest.dto.response;

import by.dudkin.common.enums.CarType;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record CarResponse(

        UUID id,

        String licensePlate,

        String model,

        CarType type,

        long year,

        String color,

        Instant createdAt,

        Instant updatedAt

) implements Serializable { }
