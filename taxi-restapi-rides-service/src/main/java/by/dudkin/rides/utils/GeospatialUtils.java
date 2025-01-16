package by.dudkin.rides.utils;

import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public abstract class GeospatialUtils {

    private static final double EARTH_RADIUS = 6371.0;
    private static final double AVERAGE_SPEED = 50.0;

    public static double calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lng1Rad = Math.toRadians(lng1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());
        double lng2Rad = Math.toRadians(lng2.doubleValue());

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lng2Rad - lng1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
            + Math.cos(lat1Rad) * Math.cos(lat2Rad)
            * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public static double calculateEstimatedTime(double distance) {
        return distance / AVERAGE_SPEED * 60;
    }

}
