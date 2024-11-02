package by.dudkin.driver.rest.dto.response;

import by.dudkin.common.enums.CarType;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record CarResponse(

        Long id,

        String licensePlate,

        String model,

        CarType type,

        Integer year,

        String color,

        Instant createdAt,

        Instant updatedAt

) implements Serializable { }
