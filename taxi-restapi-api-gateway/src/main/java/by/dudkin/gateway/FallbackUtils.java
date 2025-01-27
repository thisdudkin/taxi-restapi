package by.dudkin.gateway;

/**
 * @author Alexander Dudkin
 */
final class FallbackUtils {

    private FallbackUtils() {
        throw new AssertionError();
    }

    static final String AUTHENTICATION_FALLBACK_RESPONSE = "Authentication server is unavailable at the moment.";
    static final String DRIVER_FALLBACK_RESPONSE = "Drivers service is unavailable at the moment.";
    static final String PASSENGER_FALLBACK_RESPONSE = "Passengers service is unavailable at the moment.";
    static final String PAYMENT_FALLBACK_RESPONSE = "Payment service in unavailable at the moment.";
    static final String RIDES_FALLBACK_RESPONSE = "Rides service is unavailable at the moment.";
    static final String NOTIFICATION_FALLBACK_RESPONSE = "Notification service is unavailable at the moment.";

}
