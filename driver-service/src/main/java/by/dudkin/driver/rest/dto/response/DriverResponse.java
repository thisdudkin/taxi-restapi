package by.dudkin.driver.rest.dto.response;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.DriverStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Alexander Dudkin
 */
public record DriverResponse(

    Long id,

    PersonalInfo info,

    BigDecimal balance,

    DriverStatus status,

    Integer experience,

    Double rating,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) implements Serializable {
}
