package by.dudkin.notification.feign.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Alexander Dudkin
 */
@Service
public class RetrieveMessageErrorDecoder implements ErrorDecoder {

    private final Map<String, FeignExceptionHandler> feignExceptionHandlers;

    public RetrieveMessageErrorDecoder(Map<String, FeignExceptionHandler> feignExceptionHandlers) {
        this.feignExceptionHandlers = feignExceptionHandlers;
    }

    @Override
    public Exception decode(String s, Response response) {
        String requestUrl = response.request().url();
        FeignExceptionHandler handler = feignExceptionHandlers.get(String.valueOf(response.status()));

        if (handler != null) {
            return handler.handleException(response);
        }

        return new RuntimeException("No handler found. Generic exception for URL: " + requestUrl);
    }

}
