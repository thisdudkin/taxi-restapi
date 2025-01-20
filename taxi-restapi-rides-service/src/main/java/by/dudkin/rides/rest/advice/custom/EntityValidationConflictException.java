package by.dudkin.rides.rest.advice.custom;

public class EntityValidationConflictException extends RuntimeException {
  public EntityValidationConflictException(String message) {
    super(message);
  }
}
