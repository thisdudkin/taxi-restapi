package by.dudkin.passenger.rest.controller;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.rest.api.PassengerApi;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public ResponseEntity<PaginatedResponse<PassengerResponse>> getAll(Pageable pageable) {
        return new ResponseEntity<>(passengerService.findAll(pageable), OK);
    }

    @Override
    public ResponseEntity<PassengerResponse> get(long passengerId) {
        return new ResponseEntity<>(passengerService.findById(passengerId), OK);
    }

    @Override
    public ResponseEntity<PassengerResponse> save(PassengerRequest passengerRequest) {
        return new ResponseEntity<>(passengerService.create(passengerRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PassengerResponse> update(long passengerId, PassengerRequest passengerRequest) {
        return new ResponseEntity<>(passengerService.update(passengerId, passengerRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(long passengerId) {
        passengerService.delete(passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<BalanceResponse<Long>> checkBalance(Long passengerId) {
        return new ResponseEntity<>(passengerService.checkBalance(passengerId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateBalance(long passengerId, BigDecimal amount) {
        passengerService.updateBalance(passengerId, amount);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
