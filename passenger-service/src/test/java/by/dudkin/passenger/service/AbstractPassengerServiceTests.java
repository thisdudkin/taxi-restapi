package by.dudkin.passenger.service;

import by.dudkin.common.entity.BaseEntity;
import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.util.EntityUtils;
import by.dudkin.passenger.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;

import static by.dudkin.common.enums.PaymentMethod.CASH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Alexander Dudkin
 */
abstract class AbstractPassengerServiceTests {

    private static final String PASSENGER_USERNAME_IVAN = "ivan";
    private static final String PASSENGER_USERNAME_ANNA = "anna";
    private static final String PASSENGER_EMAIL_ANNA = "anna@example.com";
    private static final String PASSENGER_USERNAME_TAGIR = "tagir";
    private static final String PASSENGER_PASSWORD = "password";
    private static final String PASSENGER_EMAIL_TAGIR = "tagir@gmail.com";

    @Autowired
    protected PassengerService passengerService;

    @Autowired
    private PassengerMapper passengerMapper;

    @Test
    void shouldFindPassengerById() {
        PassengerDto passengerDto = this.passengerService.findById(0L);

        assertThat(passengerDto.username()).isEqualTo(PASSENGER_USERNAME_IVAN);
        assertThrows(PassengerNotFoundException.class, () -> {
            this.passengerService.findById(991L);
        });
    }

    @Test
    void shouldFindPassengers() {
        Collection<Passenger> passengers = this.passengerService.findAll().stream()
                .map(passengerMapper::toPassenger)
                .toList();

        Passenger passenger = EntityUtils.getById(passengers, Passenger.class, 2);

        assertThat(passenger.getUsername()).isEqualTo(PASSENGER_USERNAME_ANNA);
        assertThat(passenger.getEmail()).isEqualTo(PASSENGER_EMAIL_ANNA);
    }

    @Test
    @Transactional
    void shouldAddNewPassengers() {
        Collection<PassengerDto> passengers = this.passengerService.findAll();
        int found = passengers.size();
        PassengerFieldsDto dto = TestDataGenerator.createRandomFieldsDto();

        PassengerDto savedPassenger = this.passengerService.create(dto);

        assertThat(savedPassenger.id().longValue()).isNotEqualTo(0);
        assertThat(savedPassenger.createdAt()).isNotNull();
        assertThat(savedPassenger.updatedAt()).isNull();
        passengers = this.passengerService.findAll();
        assertThat(passengers.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdatePassenger() {
        PassengerDto passenger = passengerService.findById(1);
        Instant previousCreatedAt = passenger.createdAt();
        String oldUsername = passenger.username();
        String newUsername = oldUsername + "X";
        PassengerFieldsDto updated = new PassengerFieldsDto(
                newUsername,
                passenger.password(),
                passenger.email(),
                passenger.info(),
                passenger.preferredPaymentMethod()
        );

        passengerService.update(1L, updated);
        PassengerDto updatedPassenger = passengerService.findById(1);

        assertThat(updatedPassenger.username()).isEqualTo(newUsername);
        assertThat(updatedPassenger.createdAt()).isEqualTo(previousCreatedAt);
    }

    @Test
    @Transactional
    void shouldDeletePassenger() {
        PassengerFieldsDto dto = TestDataGenerator.createRandomFieldsDto();
        PassengerDto saved = this.passengerService.create(dto);
        Long passengerId = saved.id();

        passengerService.delete(passengerId);

        assertThrows(PassengerNotFoundException.class, () -> {
            this.passengerService.findById(passengerId);
        });
    }

    @Test
    void shouldNotSavePassengerWithDuplicateEmail() {
        PassengerFieldsDto passenger = TestDataGenerator.createRandomFieldsDtoWithEmail(PASSENGER_EMAIL_ANNA);

        assertThatThrownBy(() -> this.passengerService.create(passenger))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldNotSavePassengerWithDuplicateUsername() {
        PassengerFieldsDto passenger = TestDataGenerator.createRandomFieldsDtoWithUsername(PASSENGER_USERNAME_ANNA);

        assertThatThrownBy(() -> this.passengerService.create(passenger))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    void shouldNotSavePassengerWithEmptyPersonalInfo() {
        PassengerFieldsDto passenger = new PassengerFieldsDto(
                TestDataGenerator.randomUsername(),
                TestDataGenerator.randomPassword(),
                TestDataGenerator.randomEmail(),
                null,
                CASH);

        assertThatThrownBy(() -> this.passengerService.create(passenger))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldReturnEmptyCollectionWhenNoPassengersFound() {
        Collection<PassengerDto> passengers = this.passengerService.findAll();
        passengers.stream().map(passengerMapper::toPassenger)
                .map(BaseEntity::getId)
                .forEach(passengerService::delete);

        Collection<PassengerDto> result = this.passengerService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowPassengerNotFoundExceptionWhenPassengerNotFoundById() {
        assertThrows(PassengerNotFoundException.class, () -> {
            this.passengerService.findById(9999L);
        });
    }

}
