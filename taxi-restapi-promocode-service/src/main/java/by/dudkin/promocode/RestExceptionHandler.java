package by.dudkin.promocode;

import by.dudkin.common.util.ErrorMessages;
import by.dudkin.i18n.I18nUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final I18nUtils i18nUtils;
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    RestExceptionHandler(I18nUtils i18nUtils) {
        this.i18nUtils = i18nUtils;
    }

    @ExceptionHandler(PromocodeNotFoundException.class)
    ResponseEntity<ProblemDetail> handlePromocodeNotFoundException(PromocodeNotFoundException e) {
        String message = i18nUtils.getMessage(ErrorMessages.PROMOCODE_NOT_FOUND);

        logger.info("Promocode not found -> {}", e.getMessage());
        return ResponseEntity.status(404).body(forStatusAndDetail(NOT_FOUND, message));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleException(Exception e) {
        String message = i18nUtils.getMessage(ErrorMessages.GENERAL_ERROR);
        return ResponseEntity.status(500).body(forStatusAndDetail(INTERNAL_SERVER_ERROR, message));
    }

}
