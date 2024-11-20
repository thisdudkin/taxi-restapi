package by.dudkin.passenger.util;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
  * @author Alexander Dudkin
  */
public abstract class TestDataGenerator {

    private static Long randomId() {
        return ThreadLocalRandom.current().nextLong(99);
    }

    private static String randomString() {
        return RandomStringUtils.randomAlphabetic(13);
    }

    private static LocalDate randomDate() {
        return LocalDate.of(1, 1, 19);
    }

    private static BigDecimal randomBalance() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(30, 1000));
    }

    private static PaymentMethod randomPayment() {
        return (ThreadLocalRandom.current().nextInt(0, 2) < 1) ? PaymentMethod.CASH : PaymentMethod.CREDIT_CARD;
    }

    private static Double randomRating() {
        return ThreadLocalRandom.current().nextDouble(1.0, 5.0);
    }

    private static PersonalInfo randomInfo() {
        return PersonalInfo.builder()
            .firstName(randomString())
            .lastName(randomString())
            .phone(randomString())
            .dateOfBirth(randomDate())
            .build();
    }

    public static PassengerRequest randomRequestWithFirstname(String firstName) {
        return new PassengerRequest(
            PersonalInfo.builder()
                .firstName(firstName)
                .lastName(randomString())
                .phone(randomString())
                .dateOfBirth(randomDate())
                .build(),
            randomBalance(),
            randomPayment()
        );
    }

    public static PassengerRequest randomRequestWithInfo(PersonalInfo info) {
        return new PassengerRequest(
            info,
            randomBalance(),
            randomPayment()
        );
    }

    public static Passenger randomPassenger() {
        return Passenger.builder()
            .info(randomInfo())
            .balance(randomBalance())
            .preferredPaymentMethod(randomPayment())
            .build();
    }

    public static PassengerRequest randomRequest() {
        return new PassengerRequest(
            randomInfo(),
            randomBalance(),
            randomPayment()
        );
    }

    public static PassengerResponse randomResponse() {
        return new PassengerResponse(
            randomId(),
            randomInfo(),
            randomPayment(),
            randomBalance(),
            randomRating(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

}
