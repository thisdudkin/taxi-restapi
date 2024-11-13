package by.dudkin.driver.rest.advice.custom;

/**
 * @author Alexander Dudkin
 */
public class DuplicateLicensePlateException extends RuntimeException {
    public DuplicateLicensePlateException(String message) {
        super(message);
    }
}
