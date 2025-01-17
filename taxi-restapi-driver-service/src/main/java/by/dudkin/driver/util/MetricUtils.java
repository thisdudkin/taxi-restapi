package by.dudkin.driver.util;

/**
 * @author Alexander Dudkin
 */
public final class MetricUtils {

    private MetricUtils() {
        throw new AssertionError();
    }

    public static final String DRIVERS_CREATED_COUNT = "drivers_created_count";
    public static final String DRIVERS_UPDATED_COUNT = "drivers_updated_count";
    public static final String DRIVERS_LOCATION_UPDATED_COUNT = "drivers_location_updated_count";
    public static final String CARS_CREATED_COUNT = "cars_created_count";
    public static final String CARS_DELETED_COUNT = "cars_deleted_count";
    public static final String ASSIGNMENTS_CREATED_COUNT = "assignments_created_count";
    public static final String ASSIGNMENTS_CANCELLED_COUNT = "assignments_cancelled_count";
    public static final String ASSIGNMENTS_DELETED_COUNT = "assignments_deleted_count";

    public static final String DRIVERS_CREATED_COUNT_DESCRIPTION = "The number of created drivers";
    public static final String DRIVERS_UPDATED_COUNT_DESCRIPTION = "The number of updated drivers";
    public static final String DRIVERS_LOCATION_UPDATED_COUNT_DESCRIPTION = "The number of updated driver location";
    public static final String CARS_CREATED_COUNT_DESCRIPTION = "The number of created cars";
    public static final String CARS_DELETED_COUNT_DESCRIPTION = "The number of deleted cars";
    public static final String ASSIGNMENTS_CREATED_COUNT_DESCRIPTION = "The number of created assignments";
    public static final String ASSIGNMENTS_CANCELLED_COUNT_DESCRIPTION = "The number of cancelled assignments";
    public static final String ASSIGNMENTS_DELETED_COUNT_DESCRIPTION = "The number of deleted assignments";

}
