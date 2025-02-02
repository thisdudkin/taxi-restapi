package by.dudkin.auth;

import by.dudkin.auth.i18n.I18nUtils;
import by.dudkin.auth.i18n.LocaleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.NotAuthorizedException;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

/**
 * @author Alexander Dudkin
 */
@RestControllerAdvice
class RestExceptionHandler {

    private final I18nUtils i18nUtils;
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    RestExceptionHandler(I18nUtils i18nUtils) {
        this.i18nUtils = i18nUtils;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
            .collect(joining("; "));
        String message = i18nUtils.getMessage(ErrorMessages.VALIDATION_FAILED, errors);

        logger.warn("Validation failed: {}. Exception: {}", errors, e.getMessage());
        return ResponseEntity.status(400).body(forStatusAndDetail(BAD_REQUEST, message));
    }

    @ExceptionHandler(InvalidRoleException.class)
    ResponseEntity<ProblemDetail> handleInvalidRoleException(InvalidRoleException e) {
        String message = i18nUtils.getMessage(e.getMessage());

        logger.warn("Invalid role access attempt: {}", e.getMessage());
        return ResponseEntity.status(403).body(forStatusAndDetail(FORBIDDEN, message));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    ResponseEntity<ProblemDetail> handleNotAuthorizedException(NotAuthorizedException e) {
        String message = i18nUtils.getMessage(ErrorMessages.KEYCLOAK_UNAUTHORIZED);

        logger.warn("Unauthorized access attempt: {}", e.getMessage());
        return ResponseEntity.status(401).body(forStatusAndDetail(UNAUTHORIZED, message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException e) {
        HttpStatusCode httpStatusCode = e.getStatusCode();

        if (httpStatusCode.is4xxClientError()) {
            logger.warn("Client error: {} - {}", httpStatusCode.value(), e.getReason());
        } else {
            logger.error("Server error: {} - {}", httpStatusCode.value(), e.getReason());
        }

        return ResponseEntity.status(httpStatusCode.value()).body(forStatusAndDetail(httpStatusCode, e.getReason()));
    }

    @ExceptionHandler(LocaleException.class)
    ResponseEntity<ProblemDetail> handleLocaleException(LocaleException e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);

        logger.error("Locale processing error occurred", e);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);

        logger.error("Unhandled exception occurred", e);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

}
