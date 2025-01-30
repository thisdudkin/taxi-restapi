package by.dudkin.rides.feign;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.configuration.OpenFeignBearerTokenInterceptorConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "payment-service",
    path = "/api",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface PaymentClient {

    @PutMapping("/payments")
    void processTransaction(@RequestBody TransactionRequest<UUID> transactionRequest);

    @GetMapping("/accounts/{userId}")
    BalanceResponse<UUID> getBalance(@PathVariable UUID userId);

}
