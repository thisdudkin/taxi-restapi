package by.dudkin.promocode;

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
class RestExceptionHandler {

    final MessageSource messageSource;

    RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(PromocodeNotFoundException.class)
    ResponseEntity<ProblemDetail> handlePromocodeNotFoundException(PromocodeNotFoundException e) {
        String message = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, message), NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ProblemDetail> handleIllegalStateException(IllegalStateException e) {
        String message = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(CONFLICT, message), CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = messageSource.getMessage(e.getMessage(), null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(BAD_REQUEST, message), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = messageSource.getMessage(ErrorMessages.DATA_INTEGRITY, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, message), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = messageSource.getMessage(ErrorMessages.GENERAL_ERROR, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, e.getMessage()), INTERNAL_SERVER_ERROR);
    }

}
