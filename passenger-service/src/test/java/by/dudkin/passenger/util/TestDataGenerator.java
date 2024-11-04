package by.dudkin.passenger.util;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alexander Dudkin
 */
public class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static String randomUsername() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String randomEmail() {
        return String.format("%s@example.com", RandomStringUtils.randomAlphabetic(10));
    }

    public static String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public static String randomFirstName() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String randomLastName() {
        return RandomStringUtils.randomAlphabetic(7);
    }

    public static String randomPhone() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    public static BigDecimal randomBalance() {
        return BigDecimal.valueOf(new Random().nextInt(1, 4999));
    }

    public static LocalDate randomDateOfBirth() {
        int year = ThreadLocalRandom.current().nextInt(1950, 2000);
        return LocalDate.of(year, 1, 1);
    }

    public static Passenger createRandomPassenger() {
        return Passenger.builder()
                .username(randomUsername())
                .email(randomEmail())
                .password(randomPassword())
                .info(PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build())
                .preferredPaymentMethod(PaymentMethod.CASH)
                .balance(randomBalance())
                .build();
    }

    public static Passenger createRandomPassengerWithId(Long id) {
        Passenger passenger = Passenger.builder()
                .username(randomUsername())
                .email(randomEmail())
                .password(randomPassword())
                .info(PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build())
                .preferredPaymentMethod(PaymentMethod.CASH)
                .balance(randomBalance())
                .build();
        passenger.setId(id);
        return passenger;
    }

    public static PassengerRequest createRandomRequest() {
        return new PassengerRequest(
                randomUsername(),
                randomEmail(),
                randomPassword(),
                PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build(),
                randomBalance(),
                PaymentMethod.CREDIT_CARD
        );
    }

    public static PassengerRequest createRandomRequestWithUsername(String username) {
        return new PassengerRequest(
                username,
                randomEmail(),
                randomPassword(),
                PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build(),
                randomBalance(),
                PaymentMethod.CREDIT_CARD
        );
    }

    public static PassengerRequest createRandomRequestWithEmail(String email) {
        return new PassengerRequest(
                randomUsername(),
                email,
                randomPassword(),
                PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build(),
                randomBalance(),
                PaymentMethod.CREDIT_CARD
        );
    }

}
