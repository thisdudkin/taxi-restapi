package by.dudkin.rides.service;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.feign.DriverClient;
import by.dudkin.rides.feign.PaymentClient;
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
        paymentClient.processTransaction(req);
        driverClient.markDriverAvailable(ride.getDriverId());
        return ride;
    }

}
