package by.dudkin.passenger.rest.dto.response;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author Alexander Dudkin
 */
public record PassengerResponse(

        Long id,

        PersonalInfo info,

        PaymentMethod paymentMethod,

        BigDecimal balance,

        Double rating,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) implements Serializable { }
