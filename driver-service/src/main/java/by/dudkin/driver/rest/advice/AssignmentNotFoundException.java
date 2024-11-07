package by.dudkin.driver.rest.advice;

/**
 * @author Alexander Dudkin
 */
public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String message) {
        super(message);
    }
}
