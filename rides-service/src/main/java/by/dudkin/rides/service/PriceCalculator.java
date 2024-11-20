package by.dudkin.rides.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Alexander Dudkin
 */
@Component
public class PriceCalculator {

    private static final BigDecimal BASE_FARE = new BigDecimal("1.5");
    private static final BigDecimal PER_KM_RATE = new BigDecimal("1.35");
    private static final BigDecimal PER_MIN_RATE = new BigDecimal("0.05");
    private static final BigDecimal MIN_RATE = new BigDecimal("2.0");
    private static final double AVERAGE_SPEED = 50.0;

    public BigDecimal calculatePrice(double distance) {
        double duration = (distance / AVERAGE_SPEED) * 60;

        var distanceCost = PER_KM_RATE.multiply(BigDecimal.valueOf(distance));
        var timeCost = PER_MIN_RATE.multiply(BigDecimal.valueOf(duration));
        var totalPrice = BASE_FARE.add(distanceCost).add(timeCost);

        return totalPrice.max(MIN_RATE).setScale(2, RoundingMode.HALF_UP);
    }

}
