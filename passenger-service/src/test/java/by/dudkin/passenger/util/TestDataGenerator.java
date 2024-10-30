package by.dudkin.passenger.util;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                .balance(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 10000)))
                .build();
    }

    public static PassengerFieldsDto createRandomFieldsDto() {
        return new PassengerFieldsDto(
                randomUsername(),
                randomPassword(),
                randomEmail(),
                PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build(),
                PaymentMethod.CREDIT_CARD
        );
    }

    public static PassengerFieldsDto createRandomFieldsDtoWithUsername(String username) {
        return new PassengerFieldsDto(
                username,
                randomPassword(),
                randomEmail(),
                PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build(),
                PaymentMethod.CREDIT_CARD
        );
    }

    public static PassengerFieldsDto createRandomFieldsDtoWithEmail(String email) {
        return new PassengerFieldsDto(
                randomUsername(),
                randomPassword(),
                email,
                PersonalInfo.builder()
                        .firstName(randomFirstName())
                        .lastName(randomLastName())
                        .phone(randomPhone())
                        .dateOfBirth(randomDateOfBirth())
                        .build(),
                PaymentMethod.CREDIT_CARD
        );
    }

}
