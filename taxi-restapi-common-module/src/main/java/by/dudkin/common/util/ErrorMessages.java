package by.dudkin.common.util;

/**
 * @author Alexander Dudkin
 */
public final class ErrorMessages {

    private ErrorMessages() {};

    /**
     * Common Errors
     */
    public static final String VALIDATION_FAILED = "error.validation.failed";
    public static final String DATA_INTEGRITY = "error.data.integrity";
    public static final String GENERAL_ERROR = "error.general";

    /**
     * Passenger Service Errors
     */
    public static final String PASSENGER_NOT_FOUND = "error.passenger.notfound";
    public static final String PASSENGER_ALREADY_EXISTS_WITH_SAME_USERNAME = "error.passenger.already.exists.with.same.username";

    /**
     * Driver Service Errors
     */
    public static final String DRIVER_NOT_FOUND = "error.driver.notfound";
    public static final String CAR_NOT_FOUND = "error.car.notfound";
    public static final String ASSIGNMENT_NOT_FOUND = "error.assignment.notfound";
    public static final String ASSIGNMENT_ALREADY_COMPLETED = "error.assignment.completed";

    public static final String DRIVER_ALREADY_EXISTS_WITH_SAME_USERNAME = "error.driver.already.exists.with.same.username";
    public static final String DUPLICATE_LICENSE_PLATE = "error.duplicate.licensePlate";
    public static final String CAR_ALREADY_BOOKED = "error.car.already.booked";

    /**
     * Ride Service Errors
     */
    public static final String RIDE_NOT_FOUND = "error.ride.notfound";
    public static final String AVAILABLE_DRIVER_NOT_FOUND = "error.available.driver.notfound";

    /**
     * Promocode Service Errors
     */
    public static final String PROMOCODE_NOT_FOUND = "error.promocode.notfound";

    /**
     * Validation Errors
     */
    public static final String INVALID_TRANSITION = "error.validation.invalid.transition";
    public static final String DRIVER_IS_NOT_READY = "error.validation.invalid.driver.status";
    public static final String RIDE_IS_NOT_PENDING = "error.validation.invalid.ride.status";
    public static final String INSUFFICIENT_FUNDS = "error.validation.insufficient.funds";

}
