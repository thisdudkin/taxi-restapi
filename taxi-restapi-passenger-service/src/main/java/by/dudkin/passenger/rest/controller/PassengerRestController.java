package by.dudkin.passenger.rest.controller;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.aspect.TrackMetric;
import by.dudkin.passenger.rest.api.PassengerApi;
import by.dudkin.passenger.rest.dto.request.FeedbackRequest;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.passenger.service.PassengerService;
import by.dudkin.passenger.util.MetricUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

import static by.dudkin.passenger.util.MetricUtils.PASSENGERS_CREATED_COUNT;
import static by.dudkin.passenger.util.MetricUtils.PASSENGERS_UPDATED_COUNT;
import static by.dudkin.passenger.util.TokenConstants.USERNAME_CLAIM_EXPRESSION;
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
    public ResponseEntity<PassengerResponse> get(UUID passengerId) {
        return new ResponseEntity<>(passengerService.findById(passengerId), OK);
    }

    @Override
    public ResponseEntity<PassengerResponse> search(String username) {
        return new ResponseEntity<>(passengerService.findByUsername(username), OK);
    }

    @Override
    @TrackMetric(metricName = PASSENGERS_CREATED_COUNT)
    public ResponseEntity<PassengerResponse> save(PassengerRequest passengerRequest,
                                                  @AuthenticationPrincipal(expression = USERNAME_CLAIM_EXPRESSION) String username) {
        return new ResponseEntity<>(passengerService.create(passengerRequest, username), HttpStatus.CREATED);
    }

    @Override
    @TrackMetric(metricName = PASSENGERS_UPDATED_COUNT)
    public ResponseEntity<PassengerResponse> update(UUID passengerId, PassengerRequest passengerRequest) {
        return new ResponseEntity<>(passengerService.update(passengerId, passengerRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID passengerId) {
        passengerService.delete(passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PassengerResponse> ratePassenger(UUID passengerId, FeedbackRequest feedbackRequest) {
        return new ResponseEntity<>(passengerService.ratePassenger(passengerId, feedbackRequest), OK);
    }

}
