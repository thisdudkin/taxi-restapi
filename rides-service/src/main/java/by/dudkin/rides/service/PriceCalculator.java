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
    private static final double EARTH_RADIUS = 6371.0;
    private static final double AVERAGE_SPEED = 50.0;

    public BigDecimal calculatePrice(double lat1, double lng1, double lat2, double lng2) {
        double distance = calculateDistance(lat1, lng1, lat2, lng2);
        double duration = (distance / AVERAGE_SPEED) * 60;

        var distanceCost = PER_KM_RATE.multiply(BigDecimal.valueOf(distance));
        var timeCost = PER_MIN_RATE.multiply(BigDecimal.valueOf(duration));
        var totalPrice = BASE_FARE.add(distanceCost).add(timeCost);

        return totalPrice.max(MIN_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double lat1Rad = Math.toRadians(lat1);
        double lng1Rad = Math.toRadians(lng1);
        double lat2Rad = Math.toRadians(lat2);
        double lng2Rad = Math.toRadians(lng2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lng2Rad - lng1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
            + Math.cos(lat1Rad) * Math.cos(lat2Rad)
            * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

}
