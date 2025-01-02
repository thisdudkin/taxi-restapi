package by.dudkin.driver.util;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.AssignmentStatus;
import by.dudkin.common.enums.CarType;
import by.dudkin.common.enums.DriverStatus;
import by.dudkin.driver.domain.Assignment;
import by.dudkin.driver.domain.Car;
import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alexander Dudkin
 */
public class TestDataGenerator {

    private TestDataGenerator() {}

    private static String randomUsername() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private static String randomEmail() {
        return String.format("%s@example.com", RandomStringUtils.randomAlphabetic(10));
    }

    private static String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    private static String randomFirstName() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    private static String randomLastName() {
        return RandomStringUtils.randomAlphabetic(7);
    }

    private static String randomPhone() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    private static BigDecimal randomBalance() {
        return BigDecimal.valueOf(new Random().nextInt(1, 4999));
    }

    private static LocalDate randomDateOfBirth() {
        int year = ThreadLocalRandom.current().nextInt(1950, 2000);
        return LocalDate.of(year, 1, 1);
    }

    private static Integer randomExperience() {
        return ThreadLocalRandom.current().nextInt(1, 12);
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }

    private static Double randomRating() {
        return ThreadLocalRandom.current().nextDouble(1, 5);
    }

    private static String randomLicensePlate() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    private static String randomModel() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    private static int randomYear() {
        return ThreadLocalRandom.current().nextInt(2000, 2024);
    }

    private static String randomColor() {
        return RandomStringUtils.randomAlphabetic(12);
    }

    public static PersonalInfo randomInfo() {
        return PersonalInfo.builder()
            .firstName(randomFirstName())
            .lastName(randomLastName())
            .phone(randomPhone())
            .dateOfBirth(randomDateOfBirth())
            .build();
    }

    public static PersonalInfo randomInfoWithFirstname(String firstname) {
        return PersonalInfo.builder()
            .firstName(firstname)
            .lastName(randomLastName())
            .phone(randomPhone())
            .dateOfBirth(randomDateOfBirth())
            .build();
    }

    public static Driver randomDriver() {
        return Driver.builder()
            .info(randomInfo())
            .balance(randomBalance())
            .experience(randomExperience())
            .build();
    }

    public static DriverRequest randomDriverRequest() {
        return new DriverRequest(
            randomInfo(),
            randomBalance(),
            randomExperience()
        );
    }

    public static DriverRequest randomDriverRequestWithFirstname(String firstname) {
        return new DriverRequest(
            randomInfoWithFirstname(firstname),
            randomBalance(),
            randomExperience()
        );
    }


    public static DriverResponse randomDriverResponse() {
        return new DriverResponse(
            randomId(),
            randomUsername(),
            randomInfo(),
            randomBalance(),
            DriverStatus.READY,
            randomExperience(),
            randomRating(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static Car randomCar() {
        return Car.builder()
            .licensePlate(randomLicensePlate())
            .model(randomModel())
            .type(CarType.PREMIUM)
            .year(randomYear())
            .color(randomColor())
            .build();
    }

    public static CarRequest randomCarRequest() {
        return new CarRequest(
            randomLicensePlate(),
            randomModel(),
            CarType.PREMIUM,
            randomYear(),
            randomColor()
        );
    }

    public static CarRequest randomCarRequestWithColor(String color) {
        return new CarRequest(
            randomLicensePlate(),
            randomModel(),
            CarType.PREMIUM,
            randomYear(),
            color
        );
    }

    public static CarResponse randomCarResponse() {
        return new CarResponse(
            randomId(),
            randomLicensePlate(),
            randomModel(),
            CarType.PREMIUM,
            randomYear(),
            randomColor(),
            Instant.now(),
            Instant.now()
        );
    }

    public static Assignment randomAssignment(Driver driver, Car car) {
        return Assignment.builder()
            .driver(driver)
            .car(car)
            .assignmentDate(Instant.now())
            .build();
    }

    public static AssignmentRequest randomAssignmentRequest() {
        return new AssignmentRequest(
            randomLicensePlate(),
            Instant.now()
        );
    }

    public static AssignmentRequest randomAssignmentRequestWithLicensePlate(String licensePlate) {
        return new AssignmentRequest(
            licensePlate,
            Instant.now()
        );
    }

    public static AssignmentResponse randomAssignmentResponse() {
        return new AssignmentResponse(
            randomId(),
            randomDriverResponse(),
            randomCarResponse(),
            Instant.now(),
            AssignmentStatus.ACTIVE,
            Instant.now(),
            Instant.now()
        );
    }

}
