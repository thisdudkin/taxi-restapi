package by.dudkin.rides.metric;

/**
 * @author Alexander Dudkin
 */
public final class MetricUtils {

    private MetricUtils() {
        throw new AssertionError();
    }

    public static final String RIDES_ACTIVATION = "rides.activation";
    public static final String RIDES_COMPLETION = "rides.completion";
    public static final String RIDES_ASSIGNED = "rides.assigned";

}
