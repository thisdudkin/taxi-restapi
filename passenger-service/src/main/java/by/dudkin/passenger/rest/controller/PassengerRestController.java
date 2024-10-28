package by.dudkin.passenger.rest.controller;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.rest.api.PassengerApi;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class PassengerRestController implements PassengerApi {

    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @Override
    public ResponseEntity<?> listPassengers() {
        List<PassengerDto> passengers = new ArrayList<>();
        passengers.addAll(passengerMapper.toPassengerDtos(this.passengerService.findAll()));
        if (passengers.isEmpty()) {
            return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, "Passengers not found"), NOT_FOUND);
        }
        return new ResponseEntity<>(passengers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getPassenger(Integer passengerId) {
        Passenger passenger = this.passengerService.findById(passengerId);
        if (passenger == null) {
            return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, format("Passenger with ID %s not found", passengerId)), NOT_FOUND);
        }
        return new ResponseEntity<>(passengerMapper.toPassengerDto(passenger), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PassengerDto> addPassenger(PassengerFieldsDto passengerFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        Passenger passenger = passengerMapper.toPassenger(passengerFieldsDto);
        this.passengerService.save(passenger);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/passengers/{id}").buildAndExpand(passenger.getId()).toUri());
        return new ResponseEntity<>(passengerMapper.toPassengerDto(passenger), headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updatePassenger(Integer passengerId, PassengerFieldsDto passengerFieldsDto) {
        Passenger currentPassenger = this.passengerService.findById(passengerId);
        if (currentPassenger == null) {
            return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, format("Passenger with ID %s not found", passengerId)), NOT_FOUND);
        }
        currentPassenger.setUsername(passengerFieldsDto.username());
        currentPassenger.setEmail(passengerFieldsDto.email());
        currentPassenger.setPassword(passengerFieldsDto.password());
        currentPassenger.setInfo(passengerFieldsDto.info());
        currentPassenger.setPreferredPaymentMethod(passengerFieldsDto.preferredPaymentMethod());
        this.passengerService.save(currentPassenger);
        return new ResponseEntity<>(passengerMapper.toPassengerDto(currentPassenger), HttpStatus.NO_CONTENT);
    }

    @Transactional
    @Override
    public ResponseEntity<?> deletePassenger(Integer passengerId) {
        Passenger passenger = this.passengerService.findById(passengerId);
        if (passenger == null) {
            return new ResponseEntity<>(forStatusAndDetail(NOT_FOUND, format("Passenger with ID %s not found", passengerId)), NOT_FOUND);
        }
        this.passengerService.delete(passenger);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
