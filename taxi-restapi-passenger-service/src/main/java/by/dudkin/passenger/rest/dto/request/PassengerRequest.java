package by.dudkin.passenger.rest.dto.request;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record PassengerRequest(

        @NotNull
        PersonalInfo info,

        @PositiveOrZero
        BigDecimal balance,

        @NotNull
        PaymentMethod paymentMethod

) implements Serializable { }
