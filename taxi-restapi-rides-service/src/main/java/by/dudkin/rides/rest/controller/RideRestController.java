package by.dudkin.rides.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.rest.api.RidesApi;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideCostRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.request.PendingRide;
import by.dudkin.rides.rest.dto.response.RideCostResponse;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.service.api.RideService;
import by.dudkin.rides.utils.RideSpecification;
import by.dudkin.rides.utils.TokenConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static by.dudkin.rides.utils.TokenConstants.USERNAME_CLAIM_EXPRESSION;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class RideRestController implements RidesApi {

    private final RideService rideService;
    private final RideSpecification rideSpecification;

    @Override
    public ResponseEntity<RideResponse> get(UUID rideId) {
        return new ResponseEntity<>(this.rideService.read(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginatedResponse<RideResponse>> getAll(UUID passengerId, UUID driverId, UUID carId, Pageable pageable) {
        var specification = rideSpecification.getSpecification(passengerId, driverId, carId);
        return new ResponseEntity<>(this.rideService.readAll(specification, pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> save(RideRequest rideRequest,
                                             @AuthenticationPrincipal(expression = USERNAME_CLAIM_EXPRESSION) String username) {
        return new ResponseEntity<>(this.rideService.create(rideRequest, username), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RideResponse> update(UUID rideId, RideRequest rideRequest) {
        return new ResponseEntity<>(this.rideService.update(rideId, rideRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID rideId) {
        this.rideService.delete(rideId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<RideResponse> activate(UUID rideId) {
        return new ResponseEntity<>(this.rideService.activate(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> assignDriver(UUID rideId,
                                                     @AuthenticationPrincipal(expression = USERNAME_CLAIM_EXPRESSION)
                                                     String username) {
        return new ResponseEntity<>(this.rideService.assign(rideId, username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> markDone(UUID rideId) {
        return new ResponseEntity<>(this.rideService.complete(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> cancel(UUID rideId) {
        return new ResponseEntity<>(this.rideService.cancel(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> rate(UUID rideId, RideCompletionRequest request) {
        return new ResponseEntity<>(this.rideService.rate(rideId, request), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideCostResponse> checkRideCost(RideCostRequest request) {
        return new ResponseEntity<>(this.rideService.checkCost(request), HttpStatus.OK);
    }

}
