package by.dudkin.passenger.rest.controller;

import by.dudkin.passenger.rest.api.PassengerApi;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class PassengerRestController implements PassengerApi {

    private final PassengerService passengerService;

    @Override
    public ResponseEntity<List<PassengerDto>> listPassengers() {
        List<PassengerDto> passengers = new ArrayList<>(this.passengerService.findAll());
        return new ResponseEntity<>(passengers, passengers.isEmpty() ? NOT_FOUND : OK);
    }

    @Override
    public ResponseEntity<PassengerDto> getPassenger(Integer passengerId) {
        return new ResponseEntity<>(this.passengerService.findById(passengerId), OK);
    }

    @Override
    public ResponseEntity<PassengerDto> addPassenger(PassengerFieldsDto passengerFieldsDto) {
        PassengerDto savedPassenger = this.passengerService.create(passengerFieldsDto);
        return new ResponseEntity<>(savedPassenger, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PassengerDto> updatePassenger(Integer passengerId, PassengerFieldsDto passengerFieldsDto) {
        PassengerDto updatedPassenger = this.passengerService.update(passengerId, passengerFieldsDto);
        return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<PassengerDto> deletePassenger(Long passengerId) {
        this.passengerService.delete(passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
