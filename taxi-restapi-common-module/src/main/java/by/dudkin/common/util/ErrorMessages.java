package by.dudkin.common.util;

/**
 * @author Alexander Dudkin
 */
public class ErrorMessages {

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

    /**
     * Driver Service Errors
     */
    public static final String DRIVER_NOT_FOUND = "error.driver.notfound";
    public static final String CAR_NOT_FOUND = "error.car.notfound";
    public static final String ASSIGNMENT_NOT_FOUND = "error.assignment.notfound";
    public static final String ASSIGNMENT_ALREADY_COMPLETED = "error.assignment.completed";

    public static final String DUPLICATE_LICENSE_PLATE = "error.duplicate.licensePlate";
    public static final String CAR_ALREADY_BOOKED = "error.car.already.booked";

    /**
     * Ride Service Errors
     */
    public static final String RIDE_NOT_FOUND = "error.ride.notfound";
    public static final String AVAILABLE_DRIVER_NOT_FOUND = "error.available.driver.notfound";

    /**
     * Validation Errors
     */
    public static final String INVALID_TRANSITION = "error.validation.invalid.transition";
    public static final String DRIVER_IS_NOT_READY = "error.validation.invalid.driver.status";
    public static final String RIDE_IS_NOT_PENDING = "error.validation.invalid.ride.status";

}
