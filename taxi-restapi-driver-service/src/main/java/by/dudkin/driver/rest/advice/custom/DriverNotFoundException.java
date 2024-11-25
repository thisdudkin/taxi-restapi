package by.dudkin.driver.rest.advice.custom;

/**
 * @author Alexander Dudkin
 */
public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(String message) {
        super(message);
    }
}
