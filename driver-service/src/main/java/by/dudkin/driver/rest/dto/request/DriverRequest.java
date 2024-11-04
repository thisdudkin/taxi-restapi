package by.dudkin.driver.rest.dto.request;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.DriverStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record DriverRequest(

        @NotBlank
        @Size(min = 3, max = 128)
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 128)
        String password,

        @NotNull
        PersonalInfo info,

        @PositiveOrZero
        BigDecimal balance,

        @PositiveOrZero
        int experience

) implements Serializable { }
