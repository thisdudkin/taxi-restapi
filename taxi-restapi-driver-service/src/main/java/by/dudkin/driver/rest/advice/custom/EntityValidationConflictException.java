package by.dudkin.driver.rest.advice.custom;

public class EntityValidationConflictException extends RuntimeException {
    public EntityValidationConflictException(String message) {
        super(message);
    }
}
