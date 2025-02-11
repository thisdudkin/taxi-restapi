package by.dudkin.notification.feign.exception;

import feign.FeignException;
import feign.Response;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component("400")
public class FeignBadRequestExceptionHandler implements FeignExceptionHandler {

    @Override
    public FeignException handleException(Response response) {
        return FeignException.errorStatus(response.request().httpMethod().name(), response);
    }

}
