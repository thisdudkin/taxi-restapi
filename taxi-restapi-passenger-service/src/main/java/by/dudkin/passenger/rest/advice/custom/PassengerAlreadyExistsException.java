package by.dudkin.passenger.rest.advice.custom;

public class PassengerAlreadyExistsException extends RuntimeException {
  public PassengerAlreadyExistsException(String message) {
    super(message);
  }
}
