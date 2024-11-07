package by.dudkin.driver.rest.advice;

/**
 * @author Alexander Dudkin
 */
public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super(message);
    }
}
