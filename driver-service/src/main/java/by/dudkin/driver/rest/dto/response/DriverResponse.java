package by.dudkin.driver.rest.dto.response;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.DriverStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record DriverResponse(

        long id,

        String username,

        String email,

        PersonalInfo info,

        BigDecimal balance,

        DriverStatus status,

        long experience,

        double rating,

        Instant createdAt,

        Instant updatedAt

) implements Serializable {}