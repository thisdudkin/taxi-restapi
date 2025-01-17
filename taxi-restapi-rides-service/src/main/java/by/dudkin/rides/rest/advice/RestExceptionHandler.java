package by.dudkin.rides.rest.advice;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.rides.i18n.I18nUtils;
import by.dudkin.rides.rest.advice.custom.EntityValidationConflictException;
import by.dudkin.rides.rest.advice.custom.IllegalStatusTransitionException;
import by.dudkin.rides.rest.advice.custom.InsufficientFundsException;
import by.dudkin.rides.rest.advice.custom.RideNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYMENT_REQUIRED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
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

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleRideNotFoundException(RideNotFoundException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.info("Ride not found -> {}", e.getMessage());
        return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, message), NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationConflictException.class)
    public ResponseEntity<ProblemDetail> handleEntityValidationConflictException(EntityValidationConflictException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.warn("Entity validation forced conflict -> {}", e.getMessage());
        return ResponseEntity.status(409).body(forStatusAndDetail(CONFLICT, message));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ProblemDetail> handleInsufficientFundsException(InsufficientFundsException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.warn("Attempt to create a ride with insufficient funds -> {}", e.getMessage());
        return ResponseEntity.status(422).body(forStatusAndDetail(UNPROCESSABLE_ENTITY, message));
    }

    @ExceptionHandler(IllegalStatusTransitionException.class)
    public ResponseEntity<ProblemDetail> handleIllegalStatusTransitionException(IllegalStatusTransitionException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.warn("Invalid status transition -> {}", e.getMessage());
        return ResponseEntity.status(400).body(forStatusAndDetail(BAD_REQUEST, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);

        logger.error("Unhandled exception -> {}", e.getMessage(), e);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

}
