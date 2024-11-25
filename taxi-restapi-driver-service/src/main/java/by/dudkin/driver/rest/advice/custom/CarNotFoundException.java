package by.dudkin.driver.rest.advice.custom;

/**
 * @author Alexander Dudkin
 */
public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super(message);
    }
}
