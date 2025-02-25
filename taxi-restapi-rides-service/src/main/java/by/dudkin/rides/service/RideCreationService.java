package by.dudkin.rides.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.feign.PassengerClient;
import by.dudkin.rides.feign.PaymentClient;
import by.dudkin.rides.feign.PromocodeClient;
import by.dudkin.rides.rest.advice.custom.InsufficientFundsException;
import by.dudkin.rides.rest.dto.response.PassengerResponse;
import by.dudkin.rides.rest.dto.response.Promocode;
import by.dudkin.rides.utils.GeospatialUtils;
import by.dudkin.rides.utils.PriceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Slf4j
@Service
public class RideCreationService {

    private final PaymentClient paymentClient;
    private final PassengerClient passengerClient;
    private final PriceCalculator priceCalculator;
    private final PromocodeClient promocodeClient;

    public RideCreationService(PaymentClient paymentClient, PassengerClient passengerClient, PriceCalculator priceCalculator, PromocodeClient promocodeClient) {
        this.paymentClient = paymentClient;
        this.passengerClient = passengerClient;
        this.priceCalculator = priceCalculator;
        this.promocodeClient = promocodeClient;
    }

    public Ride createRide(Ride ride, String promocode, String username) {
        PassengerResponse passenger = passengerClient.getPassengerByUsername(username);
        ride.setPassengerId(passenger.id());
        double distance = GeospatialUtils.calculateDistance(ride.getFrom().getLat(), ride.getFrom().getLng(),
            ride.getTo().getLat(), ride.getTo().getLng());
        BigDecimal price = priceCalculator.calculatePrice(distance);

        validatePassenger(ride.getPassengerId(), price);

        if (promocode != null && !promocode.isEmpty()) {
            price = applyPromocode(price, promocode);
        }

        ride.setPrice(price);
        return ride;
    }

    private BigDecimal applyPromocode(BigDecimal price, String code) {
        Promocode promocode = promocodeClient.validate(code);
        if (promocode != null) {
            BigDecimal discountPercentage = BigDecimal.valueOf(promocode.discount()).divide(BigDecimal.valueOf(100));
            BigDecimal discount = price.multiply(discountPercentage);
            BigDecimal discountedPrice = price.subtract(discount);
            return discountedPrice.setScale(2, RoundingMode.CEILING);
        }
        return price.setScale(2, RoundingMode.CEILING);
    }

    private void validatePassenger(UUID passengerId, BigDecimal amount) {
        BalanceResponse<UUID> response = paymentClient.getBalance(passengerId);
        if (response.amount().compareTo(amount) < 0) {
            throw new InsufficientFundsException(ErrorMessages.INSUFFICIENT_FUNDS);
        }
    }

}
