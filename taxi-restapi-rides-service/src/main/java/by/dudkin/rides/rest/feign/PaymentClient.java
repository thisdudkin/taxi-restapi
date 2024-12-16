package by.dudkin.rides.rest.feign;

import by.dudkin.common.util.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "payment-service",
    path = "/api/payments"
)
public interface PaymentClient {

    @PutMapping
    void processTransaction(@RequestBody TransactionRequest<Long> transactionRequest);

}
