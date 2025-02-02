package by.dudkin.notification.feign.exception;

import feign.FeignException;
import feign.Response;

/**
 * @author Alexander Dudkin
 */
public interface FeignExceptionHandler {
    FeignException handleException(Response response);
}
