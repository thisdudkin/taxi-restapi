package by.dudkin.passenger.rest.advice;

import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.common.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Arrays;
import java.util.Locale;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

/**
 * @author Alexander Dudkin
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .collect(joining("; "));
        String message = messageSource.getMessage(ErrorMessages.VALIDATION_FAILED, new Object[]{errors}, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(BAD_REQUEST, message), BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        String errors = Arrays.stream(requireNonNull(e.getDetailMessageArguments()))
                .map(Object::toString)
                .collect(joining("; "));
        String message = messageSource.getMessage(ErrorMessages.VALIDATION_FAILED, new Object[]{errors}, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(BAD_REQUEST, message), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = messageSource.getMessage(ErrorMessages.DATA_INTEGRITY, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, message), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePassengerNotFoundException(PassengerNotFoundException e) {
        String message = messageSource.getMessage(ErrorMessages.PASSENGER_NOT_FOUND, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, message), NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = messageSource.getMessage(ErrorMessages.GENERAL_ERROR, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, message), INTERNAL_SERVER_ERROR);
    }

}
