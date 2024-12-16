package by.dudkin.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
@FeignClient(
    value = "passengers-service",
    path = "/api/passengers"
)
public interface PassengerClient {

    @PutMapping("/{passengerId}/balance")
    void updateBalance(@PathVariable long passengerId, @RequestParam BigDecimal amount);

}
