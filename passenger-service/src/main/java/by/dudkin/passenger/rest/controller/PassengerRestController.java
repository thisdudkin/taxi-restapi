package by.dudkin.passenger.rest.controller;

import by.dudkin.passenger.rest.api.PassengerApi;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class PassengerRestController implements PassengerApi {

    private final PassengerService passengerService;

    @Override
    public ResponseEntity<List<PassengerDto>> getAll() {
        List<PassengerDto> passengers = new ArrayList<>(passengerService.findAll());
        return new ResponseEntity<>(passengers, OK);
    }

    @Override
    public ResponseEntity<PassengerDto> get(long passengerId) {
        return new ResponseEntity<>(passengerService.findById(passengerId), OK);
    }

    @Override
    public ResponseEntity<PassengerDto> save(PassengerFieldsDto passengerFieldsDto) {
        return new ResponseEntity<>(passengerService.create(passengerFieldsDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PassengerDto> update(long passengerId, PassengerFieldsDto passengerFieldsDto) {
        return new ResponseEntity<>(passengerService.update(passengerId, passengerFieldsDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PassengerDto> deletePassenger(Long passengerId) {
        this.passengerService.delete(passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
