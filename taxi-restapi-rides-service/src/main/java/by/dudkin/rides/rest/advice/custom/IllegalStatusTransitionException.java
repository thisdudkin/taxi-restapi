package by.dudkin.rides.rest.advice.custom;

public class IllegalStatusTransitionException extends RuntimeException {
  public IllegalStatusTransitionException(String message) {
    super(message);
  }
}
