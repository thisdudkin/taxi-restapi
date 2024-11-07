package by.dudkin.driver.rest.advice;

/**
 * @author Alexander Dudkin
 */
public class DuplicateLicensePlateException extends RuntimeException {
    public DuplicateLicensePlateException(String message) {
        super(message);
    }
}
