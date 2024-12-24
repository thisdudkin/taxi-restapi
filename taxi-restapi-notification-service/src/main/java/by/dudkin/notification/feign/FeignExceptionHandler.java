package by.dudkin.notification.feign;

import feign.FeignException;
import feign.Response;

/**
 * @author Alexander Dudkin
 */
public interface FeignExceptionHandler {
    FeignException handleException(Response response);
}
