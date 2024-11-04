package by.dudkin.driver.rest.advice;

/**
 * @author Alexander Dudkin
 */
public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(String message) {
        super(message);
    }
}
