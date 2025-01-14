package by.dudkin.payment.feign;

import by.dudkin.payment.config.OpenFeignBearerTokenInterceptorConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "passengers-service",
    path = "/api/passengers",
    configuration = OpenFeignBearerTokenInterceptorConfiguration.class
)
public interface PassengerClient {

    @PutMapping("/{passengerId}/balance")
    void updateBalance(@PathVariable UUID passengerId, @RequestParam BigDecimal amount);

}
