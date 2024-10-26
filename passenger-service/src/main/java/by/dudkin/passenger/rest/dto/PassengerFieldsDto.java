package by.dudkin.passenger.rest.dto;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link by.dudkin.passenger.entity.Passenger}
 */
public record PassengerFieldsDto(@Pattern(regexp = "^[a-zA-Z]*$") @NotBlank String username,
                                 @Size(min = 8) String password,
                                 @Email @NotBlank String email,
                                 PersonalInfo info,
                                 PaymentMethod preferredPaymentMethod
) implements Serializable { }
