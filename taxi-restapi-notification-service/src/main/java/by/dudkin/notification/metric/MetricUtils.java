package by.dudkin.notification.metric;

/**
 * @author Alexander Dudkin
 */
public final class MetricUtils {

    private MetricUtils() {
        throw new AssertionError();
    }

    public static final String RIDE_REQUESTS_COUNT = "ride.requests.count";
    public static final String ACTIVE_LONG_POLLING_METRIC = "active_long_polling_connections";

}
