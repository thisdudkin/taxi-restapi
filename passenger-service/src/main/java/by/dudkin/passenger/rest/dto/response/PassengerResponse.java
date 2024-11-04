package by.dudkin.passenger.rest.dto.response;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Alexander Dudkin
 */
public record PassengerResponse(

        long id,

        String username,

        String email,

        PersonalInfo info,

        PaymentMethod paymentMethod,

        BigDecimal balance,

        double rating,

        Instant createdAt,

        Instant updatedAt

) implements Serializable { }
