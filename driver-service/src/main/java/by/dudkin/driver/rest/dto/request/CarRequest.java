package by.dudkin.driver.rest.dto.request;

import by.dudkin.common.enums.CarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record CarRequest(

        @NotBlank
        @Size(min = 6, max = 10)
        String licensePlate,

        @NotBlank
        @Size(min = 1, max = 50)
        String model,

        @NotNull
        CarType type,

        @NotNull
        @PastOrPresent
        Integer year,

        @NotBlank
        @Size(min = 3, max = 20)
        String color

) implements Serializable { }
