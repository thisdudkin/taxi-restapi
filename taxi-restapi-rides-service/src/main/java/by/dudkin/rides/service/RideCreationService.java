package by.dudkin.rides.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.feign.PassengerClient;
import by.dudkin.rides.utils.GeospatialUtils;
import by.dudkin.rides.utils.PriceCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
@Service
public class RideCreationService {

    private final PassengerClient passengerClient;
    private final PriceCalculator priceCalculator;

    public RideCreationService(PassengerClient passengerClient, PriceCalculator priceCalculator) {
        this.passengerClient = passengerClient;
        this.priceCalculator = priceCalculator;
    }

    public Ride createRide(Ride ride) {
        double distance = GeospatialUtils.calculateDistance(ride.getFrom().getLat(), ride.getFrom().getLng(),
            ride.getTo().getLat(), ride.getTo().getLng());
        BigDecimal price = priceCalculator.calculatePrice(distance);
        validatePassenger(ride.getPassengerId(), price);
        ride.setPrice(price);
        return ride;
    }

    private void validatePassenger(long passengerId, BigDecimal amount) {
        BalanceResponse<Long> response = passengerClient.checkBalance(passengerId);
        if (response.amount().compareTo(amount) < 0) {
            throw new IllegalStateException(ErrorMessages.INSUFFICIENT_FUNDS);
        }
    }

}
