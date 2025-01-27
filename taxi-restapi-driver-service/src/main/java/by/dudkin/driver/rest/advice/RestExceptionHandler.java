package by.dudkin.driver.rest.advice;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.driver.i18n.I18nUtils;
import by.dudkin.driver.rest.advice.custom.AssignmentNotFoundException;
import by.dudkin.driver.rest.advice.custom.CarNotFoundException;
import by.dudkin.driver.rest.advice.custom.DriverAlreadyExistsException;
import by.dudkin.driver.rest.advice.custom.DriverNotFoundException;
import by.dudkin.driver.rest.advice.custom.DuplicateLicensePlateException;
import by.dudkin.driver.rest.advice.custom.EntityValidationConflictException;
import by.dudkin.driver.rest.advice.custom.NoAvailableDriverException;
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
import static org.springframework.http.HttpStatus.CONFLICT;
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

    @ExceptionHandler(NoAvailableDriverException.class)
    public ResponseEntity<ProblemDetail> handleNoAvailableDriverException(NoAvailableDriverException e) {
        String message = i18nUtils.getMessage(ErrorMessages.AVAILABLE_DRIVER_NOT_FOUND);

        logger.info("No available drivers found for assignment request: {}", e.getMessage());
        return ResponseEntity.status(404).body(forStatusAndDetail(NOT_FOUND, message));
    }

    @ExceptionHandler(EntityValidationConflictException.class)
    public ResponseEntity<ProblemDetail> handleEntityValidationException(EntityValidationConflictException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.warn("Entity validation conflict occurred: {}", e.getMessage());
        return ResponseEntity.status(409).body(forStatusAndDetail(CONFLICT, message));
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleAssignmentNotFoundException(AssignmentNotFoundException e) {
        String message = i18nUtils.getMessage(ErrorMessages.ASSIGNMENT_NOT_FOUND);

        logger.info("Assignment not found: {}", e.getMessage());
        return ResponseEntity.status(404).body(forStatusAndDetail(NOT_FOUND, message));
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCarNotFoundException(CarNotFoundException e) {
        String message = i18nUtils.getMessage(ErrorMessages.CAR_NOT_FOUND);

        logger.info("Car not found: {}", e.getMessage());
        return ResponseEntity.status(404).body(forStatusAndDetail(NOT_FOUND, message));
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleDriverNotFoundException(DriverNotFoundException e) {
        String message = i18nUtils.getMessage(ErrorMessages.DRIVER_NOT_FOUND);

        logger.info("Driver not found: {}", e.getMessage());
        return ResponseEntity.status(404).body(forStatusAndDetail(NOT_FOUND, message));
    }

    @ExceptionHandler(DuplicateLicensePlateException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateLicensePlateException(DuplicateLicensePlateException e) {
        String message = i18nUtils.getMessage(ErrorMessages.DUPLICATE_LICENSE_PLATE);

        logger.warn("Attempt to create car with duplicate license plate: {}", e.getMessage());
        return ResponseEntity.status(409).body(forStatusAndDetail(CONFLICT, message));
    }

    @ExceptionHandler(DriverAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleDriverAlreadyExistsException(DriverAlreadyExistsException e) {
        String message = i18nUtils.getMessage(ErrorMessages.DRIVER_ALREADY_EXISTS_WITH_SAME_USERNAME);

        logger.warn("Attempt to create driver with duplicate username: {}", e.getMessage());
        return ResponseEntity.status(409).body(forStatusAndDetail(CONFLICT, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);

        logger.error("Unhandled exception occurred", e);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

}
