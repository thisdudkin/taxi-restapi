package by.dudkin.rides.rest.dto.response;

import by.dudkin.common.util.Location;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record RideCostResponse(
    BigDecimal cost,
    @JsonProperty("estimatedTime") int estimatedTime,
    Location from,
    Location to,
    @JsonProperty("currency") String currency
) implements Serializable {
    public RideCostResponse(BigDecimal cost, double estimatedTime, Location from, Location to) {
        this(cost, (int) Math.ceil(estimatedTime), from, to, "BYN");
    }
}
