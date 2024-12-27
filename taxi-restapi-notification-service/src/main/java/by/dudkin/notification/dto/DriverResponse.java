package by.dudkin.notification.dto;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.DriverStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record DriverResponse(

    UUID id,

    PersonalInfo info,

    BigDecimal balance,

    DriverStatus status,

    Integer experience,

    Double rating,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) implements Serializable {}
