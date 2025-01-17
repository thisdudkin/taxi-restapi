package by.dudkin.passenger.util;

/**
 * @author Alexander Dudkin
 */
public final class MetricUtils {

    private MetricUtils() {
        throw new AssertionError();
    }

    public static final String PASSENGERS_CREATED_COUNT = "passengers_created_count";
    public static final String PASSENGERS_UPDATED_COUNT = "passengers_updated_count";

    public static final String PASSENGERS_CREATED_COUNT_DESCRIPTION = "The number of created passengers";
    public static final String PASSENGERS_UPDATED_COUNT_DESCRIPTION = "The number of updated passengers";

}
