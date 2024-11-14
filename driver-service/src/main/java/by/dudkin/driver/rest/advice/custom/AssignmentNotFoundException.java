package by.dudkin.driver.rest.advice.custom;

/**
 * @author Alexander Dudkin
 */
public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String message) {
        super(message);
    }
}
