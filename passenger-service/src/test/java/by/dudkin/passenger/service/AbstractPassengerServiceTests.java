package by.dudkin.passenger.service;

import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

import static by.dudkin.common.enums.PaymentMethod.CASH;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Alexander Dudkin
 */
abstract class AbstractPassengerServiceTests {

    @Autowired
    protected PassengerService passengerService;

    @Test
    void shouldFindPassengerById() {
        Passenger passenger = this.passengerService.findById(0L);
        assertThat(passenger.getUsername()).isEqualTo("ivan");

        passenger = this.passengerService.findById(991L);
        assertThat(passenger).isNull();
    }

    @Test
    void shouldFindPassengers() {
        Collection<Passenger> passengers = this.passengerService.findAll();

        Passenger passenger = EntityUtils.getById(passengers, Passenger.class, 2);
        assertThat(passenger.getUsername()).isEqualTo("anna");
        assertThat(passenger.getEmail()).isEqualTo("anna@example.com");
    }

    @Test
    @Transactional
    void shouldAddNewPassengers() {
        Collection<Passenger> passengers = this.passengerService.findAll();
        int found = passengers.size();

        Passenger passenger = Passenger.builder()
            .username("evgeny")
            .email("jeka@gmail.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Evgeny")
                .lastName("Borisov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(1978, 1, 1))
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(2000.00))
            .build();
        this.passengerService.save(passenger);
        assertThat(passenger.getId().longValue()).isNotEqualTo(0);
        assertThat(passenger.getCreatedAt()).isNotNull();
        assertThat(passenger.getUpdatedAt()).isNull();
        passengers = this.passengerService.findAll();
        assertThat(passengers.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdatePassenger() {
        Passenger passenger = this.passengerService.findById(1);
        Instant previous = passenger.getCreatedAt();
        String oldUsername = passenger.getUsername();
        String newUsername = oldUsername + "X";
        passenger.setUsername(newUsername);
        this.passengerService.save(passenger);
        passenger = this.passengerService.findById(1);
        assertThat(passenger.getUsername()).isEqualTo(newUsername);
        assertThat(passenger.getCreatedAt()).isEqualTo(previous);
    }

    @Test
    @Transactional
    void shouldDeletePassenger() {
        Passenger passenger = Passenger.builder()
            .username("evgeny")
            .email("jeka@gmail.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Evgeny")
                .lastName("Borisov")
                .phone("Terminator")
                .dateOfBirth(LocalDate.of(1978, 1, 1))
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(2000.00))
            .build();
        this.passengerService.save(passenger);
        Long passengerId = passenger.getId();
        assertThat(passengerId).isNotNull();
        passenger = this.passengerService.findById(passengerId);
        assertThat(passenger).isNotNull();
        this.passengerService.delete(passenger);
        try {
            passenger = this.passengerService.findById(passengerId);
        } catch (Exception e) {
            passenger = null;
        }
        assertThat(passenger).isNull();
    }

    @Test
    void shouldNotSavePassengerWithDuplicateEmail() {
        Passenger passenger = Passenger.builder()
            .username("testuser")
            .email("anna@example.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Test")
                .lastName("User")
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(500.00))
            .build();

        assertThatThrownBy(() -> this.passengerService.save(passenger))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldNotSavePassengerWithDuplicateUsername() {
        Passenger passenger = Passenger.builder()
            .username("ivan")
            .email("test@gmail.com")
            .password("password")
            .info(PersonalInfo.builder()
                .firstName("Test")
                .lastName("User")
                .build())
            .preferredPaymentMethod(CASH)
            .balance(valueOf(500.00))
            .build();

        assertThatThrownBy(() -> this.passengerService.save(passenger))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    void shouldNotSavePassengerWithEmptyPersonalInfo() {
        Passenger passenger = Passenger.builder()
            .username("minimal")
            .email("minimal@example.com")
            .password("minPassword")
            .preferredPaymentMethod(CASH)
            .build();

        assertThatThrownBy(() -> this.passengerService.save(passenger))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldReturnEmptyCollectionWhenNoPassengersFound() {
        Collection<Passenger> passengers = this.passengerService.findAll();
        passengers.forEach(passengerService::delete);

        Collection<Passenger> result = this.passengerService.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNullWhenPassengerNotFoundById() {
        Passenger passenger = this.passengerService.findById(9999L);
        assertThat(passenger).isNull();
    }

}
