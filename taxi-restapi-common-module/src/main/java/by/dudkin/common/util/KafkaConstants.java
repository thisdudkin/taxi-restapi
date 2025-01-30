package by.dudkin.common.util;

/**
  * @author Alexander Dudkin
  */
public final class KafkaConstants {

    private KafkaConstants() {}

    public static final String RIDE_REQUESTS_TOPIC = "ride-requests";
    public static final String AVAILABLE_DRIVERS_TOPIC = "available-drivers";
    public static final String ACCEPTED_RIDES_TOPIC = "accepted-rides";
    public static final String DRIVER_ACCOUNT_REQUESTS_TOPIC = "driver-account-requests";
    public static final String PASSENGER_ACCOUNT_REQUESTS_TOPIC = "passenger-account-requests";

    public static final int PARTITIONS_COUNT = 1;
    public static final int REPLICAS_COUNT = 1;

}
