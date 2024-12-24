package by.dudkin.rides.feign;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.configuration.OpenFeignBearerTokenInterceptorConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "payment-service",
    path = "/api/payments",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface PaymentClient {

    @PutMapping
    void processTransaction(@RequestBody TransactionRequest<UUID> transactionRequest);

}
