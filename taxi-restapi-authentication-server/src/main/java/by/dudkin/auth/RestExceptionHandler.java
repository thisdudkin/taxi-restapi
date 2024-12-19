package by.dudkin.auth;

import by.dudkin.common.util.ErrorMessages;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException e) {
        String message = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(forStatusAndDetail(e.getStatusCode(), message), e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = messageSource.getMessage(ErrorMessages.GENERAL_ERROR, null, Locale.getDefault());
        return new ResponseEntity<>(forStatusAndDetail(INTERNAL_SERVER_ERROR, message), INTERNAL_SERVER_ERROR);
    }

}
