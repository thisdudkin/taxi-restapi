package by.dudkin.rides.service;

import by.dudkin.common.util.TransactionRequest;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.feign.DriverClient;
import by.dudkin.rides.rest.feign.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
public class RideCompletionService {

    private final DriverClient driverClient;
    private final PaymentClient paymentClient;

    public Ride completeRide(Ride ride) {
        ride.setEndTime(LocalDateTime.now());
        TransactionRequest<Long> req = new TransactionRequest<>(ride.getDriverId(), ride.getPassengerId(), ride.getId(), ride.getPrice());
        paymentClient.processTransaction(req);
        driverClient.markDriverAvailable(ride.getDriverId());
        return ride;
    }

}
