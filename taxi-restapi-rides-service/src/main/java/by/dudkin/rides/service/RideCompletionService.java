package by.dudkin.rides.service;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.feign.DriverClient;
import by.dudkin.rides.feign.PaymentClient;
import by.dudkin.rides.feign.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RideCompletionService {

    private final DriverClient driverClient;
    private final PaymentClient paymentClient;

    public Ride completeRide(Ride ride) {
        ride.setEndTime(LocalDateTime.now());
        TransactionRequest<UUID> req = new TransactionRequest<>(ride.getDriverId(), ride.getPassengerId(), ride.getId(), ride.getPrice());
        processTransaction(req);
        markDriverAvailable(ride);
        return ride;
    }

    @CircuitBreaker(name = "payment-service", fallbackMethod = "fallbackProcessTransaction")
    private void processTransaction(TransactionRequest<UUID> req) {
        paymentClient.processTransaction(req);
    }

    @CircuitBreaker(name = "driver-service", fallbackMethod = "fallbackDriverAvailable")
    private void markDriverAvailable(Ride ride) {
        driverClient.markDriverAvailable(ride.getDriverId());
    }

    private void fallbackProcessTransaction(Exception ex) {
        log.warn("Fallback method was triggered for processTransaction", ex);
        throw new ServiceUnavailableException("Payment service is unavailable.");
    }

    private void fallbackDriverAvailable(Exception ex) {
        log.warn("Fallback method was triggered for markDriverAvailable", ex);
        throw new ServiceUnavailableException("Driver service is unavailable.");
    }

}
