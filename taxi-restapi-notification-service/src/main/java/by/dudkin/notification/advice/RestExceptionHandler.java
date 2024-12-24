package by.dudkin.notification.advice;

import by.dudkin.common.util.ErrorMessages;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

/**
 * @author Alexander Dudkin
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleIllegalStateException(IllegalStateException e) {
        String message = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(CONFLICT, message), CONFLICT);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleDriverNotFoundException(DriverNotFoundException e) {
        String message = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, message), NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(BAD_REQUEST, message), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = messageSource.getMessage(ErrorMessages.DATA_INTEGRITY, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, message), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = messageSource.getMessage(ErrorMessages.GENERAL_ERROR, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, e.getMessage()), INTERNAL_SERVER_ERROR);
    }

}
