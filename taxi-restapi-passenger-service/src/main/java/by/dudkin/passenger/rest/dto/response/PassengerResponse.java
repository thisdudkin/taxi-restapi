package by.dudkin.passenger.rest.dto.response;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record PassengerResponse(

        UUID id,

        String username,

        PersonalInfo info,

        PaymentMethod paymentMethod,

        Double rating,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) implements Serializable { }
