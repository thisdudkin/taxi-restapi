package by.dudkin.passenger.service;

import by.dudkin.common.entity.BaseEntity;
import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static by.dudkin.common.enums.PaymentMethod.CASH;
import static java.lang.Math.toIntExact;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Alexander Dudkin
 */
abstract class AbstractPassengerServiceTests {

    @Autowired
    protected PassengerService passengerService;

    @Autowired
    private PassengerMapper passengerMapper;

    @Test
    void shouldFindPassengerById() {
        PassengerDto passengerDto = this.passengerService.findById(0L);
        assertThat(passengerDto.username()).isEqualTo("ivan");

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
        assertThat(passenger.getUsername()).isEqualTo("anna");
        assertThat(passenger.getEmail()).isEqualTo("anna@example.com");
    }

    @Test
    @Transactional
    void shouldAddNewPassengers() {
        Collection<PassengerDto> passengers = this.passengerService.findAll();
        int found = passengers.size();

        PassengerFieldsDto dto = new PassengerFieldsDto(
            "tagir",
            "password",
            "tagir@gmail.com",
            PersonalInfo.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(2000, 1, 19))
                .build(),
            CASH);
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
        PassengerDto passenger = this.passengerService.findById(1);
        Instant previous = passenger.createdAt();
        String oldUsername = passenger.username();
        String newUsername = oldUsername + "X";
        PassengerFieldsDto updated = new PassengerFieldsDto(
            newUsername,
            passenger.password(),
            passenger.email(),
            passenger.info(),
            passenger.preferredPaymentMethod()
        );
        this.passengerService.update(1, updated);
        passenger = this.passengerService.findById(1);
        assertThat(passenger.username()).isEqualTo(newUsername);
        assertThat(passenger.createdAt()).isEqualTo(previous);
    }

    @Test
    @Transactional
    void shouldDeletePassenger() {
        PassengerFieldsDto dto = new PassengerFieldsDto(
            "tagir",
            "password",
            "tagir@gmail.com",
            PersonalInfo.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(2000, 1, 19))
                .build(),
            CASH);
        PassengerDto saved = this.passengerService.create(dto);
        Long passengerId = saved.id();
        assertThat(passengerId).isNotNull();
        PassengerDto passenger = this.passengerService.findById(passengerId);
        assertThat(passenger).isNotNull();
        this.passengerService.delete(passenger.id());
        try {
            passenger = this.passengerService.findById(passengerId);
        } catch (Exception e) {
            passenger = null;
        }
        assertThat(passenger).isNull();
    }

    @Test
    void shouldNotSavePassengerWithDuplicateEmail() {
        PassengerFieldsDto passenger = new PassengerFieldsDto(
            "tagir",
            "password",
            "ivan@gmail.com",
            PersonalInfo.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(2000, 1, 19))
                .build(),
            CASH);

        assertThatThrownBy(() -> this.passengerService.create(passenger))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldNotSavePassengerWithDuplicateUsername() {
        PassengerFieldsDto passenger = new PassengerFieldsDto(
            "ivan",
            "password",
            "ivan@gmail.com",
            PersonalInfo.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(2000, 1, 19))
                .build(),
            CASH);

        assertThatThrownBy(() -> this.passengerService.create(passenger))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    void shouldNotSavePassengerWithEmptyPersonalInfo() {
        PassengerFieldsDto passenger = new PassengerFieldsDto(
            "tagir",
            "password",
            "ivan@gmail.com",
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
