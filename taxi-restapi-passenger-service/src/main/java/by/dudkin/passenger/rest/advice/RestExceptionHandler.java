package by.dudkin.passenger.rest.advice;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.passenger.i18n.I18nUtils;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
public class RestExceptionHandler {

    private final I18nUtils i18nUtils;
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .collect(joining("; "));
        String message = i18nUtils.getMessage(ErrorMessages.VALIDATION_FAILED, errors);

        logger.warn("Validation failed: {}. Exception: {}", errors, e.getMessage());
        return ResponseEntity.status(400).body(forStatusAndDetail(BAD_REQUEST, message));
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePassengerNotFoundException(PassengerNotFoundException e) {
        String message = i18nUtils.getMessage(ErrorMessages.PASSENGER_NOT_FOUND);

        logger.info("Passenger not found -> {}", e.getMessage());
        return ResponseEntity.status(404).body(forStatusAndDetail(NOT_FOUND, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);

        logger.error("Unhandled error -> {}", e.getMessage(), e);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

}
