package by.dudkin.notification.feign;

import feign.FeignException;
import feign.Response;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component("404")
public class FeignNotFoundExceptionHandler implements FeignExceptionHandler {

    @Override
    public FeignException handleException(Response response) {
        return FeignException.errorStatus(response.request().httpMethod().name(), response);
    }

}
