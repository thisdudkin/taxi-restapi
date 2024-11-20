package by.dudkin.driver.rest.dto.request;

import by.dudkin.common.entity.PersonalInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record DriverRequest(

    @NotNull
    PersonalInfo info,

    @PositiveOrZero
    BigDecimal balance,

    @PositiveOrZero
    Integer experience

) implements Serializable {
}
