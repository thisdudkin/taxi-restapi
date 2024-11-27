package by.dudkin.rides.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.rest.api.RidesApi;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.response.PendingRide;
import by.dudkin.rides.rest.dto.response.RideResponse;
import by.dudkin.rides.service.api.RideService;
import by.dudkin.rides.utils.RideSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RideResponse> get(Long rideId) {
        return new ResponseEntity<>(this.rideService.read(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginatedResponse<RideResponse>> getAll(Long passengerId, Long driverId, Long carId, Pageable pageable) {
        var specification = rideSpecification.getSpecification(passengerId, driverId, carId);
        return new ResponseEntity<>(this.rideService.readAll(specification, pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<PendingRide>> getPendingRides(Pageable pageable) {
        return new ResponseEntity<>(this.rideService.findAllPendingRides(pageable), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> save(RideRequest rideRequest) {
        return new ResponseEntity<>(this.rideService.create(rideRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RideResponse> update(Long rideId, RideRequest rideRequest) {
        return new ResponseEntity<>(this.rideService.update(rideId, rideRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long rideId) {
        this.rideService.delete(rideId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<RideResponse> activate(Long rideId) {
        return new ResponseEntity<>(this.rideService.activate(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> assignDriver(Long rideId, AvailableDriver availableDriver) {
        return new ResponseEntity<>(this.rideService.assign(rideId, availableDriver), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> markDone(Long rideId) {
        return new ResponseEntity<>(this.rideService.complete(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> cancel(Long rideId) {
        return new ResponseEntity<>(this.rideService.cancel(rideId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RideResponse> rate(Long rideId, RideCompletionRequest request) {
        return new ResponseEntity<>(this.rideService.rate(rideId, request), HttpStatus.OK);
    }

}
