package by.dudkin.rides.util;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.Location;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.RideResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alexander Dudkin
 */
public abstract class TestDataGenerator {

    private static Long randomId() {
        return ThreadLocalRandom.current().nextLong(1, 101);
    }

    private static RideStatus randomStatus() {
        RideStatus[] values = RideStatus.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    private static Location randomLocation() {
        return Location.builder()
                .country(RandomStringUtils.randomAlphabetic(12))
                .city(RandomStringUtils.randomAlphabetic(6))
                .street(RandomStringUtils.randomAlphabetic(12))
                .lat(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()))
                .lng(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()))
                .build();
    }

    private static BigDecimal randomPrice() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 15));
    }

    private static PaymentMethod randomPaymentMethod() {
        return (ThreadLocalRandom.current().nextInt(1, 3) < 2) ? PaymentMethod.CASH : PaymentMethod.CREDIT_CARD;
    }

    private static LocalDateTime randomTime() {
        int random = ThreadLocalRandom.current().nextInt(1, 15);
        return LocalDateTime.now().plusMinutes(random);
    }

    private static Integer randomRating() {
        return ThreadLocalRandom.current().nextInt(1, 11);
    }

    public static Ride randomRide() {
        return Ride.builder()
                .id(randomId())
                .passengerId(randomId())
                .driverId(randomId())
                .carId(randomId())
                .status(randomStatus())
                .from(randomLocation())
                .to(randomLocation())
                .price(randomPrice())
                .paymentMethod(randomPaymentMethod())
                .startTime(LocalDateTime.now())
                .endTime(randomTime())
                .rating(randomRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static RideRequest randomRideRequest() {
        return new RideRequest(
                randomId(),
                randomLocation(),
                randomLocation(),
                randomPaymentMethod()
        );
    }

    public static RideResponse randomRideResponse() {
        return new RideResponse(
                randomId(),
                randomId(),
                randomId(),
                randomId(),
                randomStatus(),
                randomLocation(),
                randomLocation(),
                randomPrice(),
                randomPaymentMethod(),
                LocalDateTime.now(),
                randomTime(),
                randomRating(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
