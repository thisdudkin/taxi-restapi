package by.dudkin.notification.advice;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.notification.i18n.I18nUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

/**
 * @author Alexander Dudkin
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private final I18nUtils i18nUtils;
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    public RestExceptionHandler(I18nUtils i18nUtils) {
        this.i18nUtils = i18nUtils;
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleDriverNotFoundException(DriverNotFoundException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.info("Driver not found for the request -> {}", e.getMessage());
        return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, message), NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);

        logger.error("Unhandled error -> {}", e.getMessage(), e);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

}
