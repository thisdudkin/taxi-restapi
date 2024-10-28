package by.dudkin.passenger.rest.dto;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link by.dudkin.passenger.entity.Passenger}
 */
public record PassengerDto(Long id,
                           @Pattern(regexp = "^[a-zA-Z]*$") @NotBlank String username,
                           @Size(min = 8) String password,
                           @Email @NotBlank String email,
                           PersonalInfo info,
                           PaymentMethod preferredPaymentMethod,
                           BigDecimal balance,
                           Double averageRating,
                           @NotNull Instant createdAt,
                           Instant updatedAt
) implements Serializable { }
