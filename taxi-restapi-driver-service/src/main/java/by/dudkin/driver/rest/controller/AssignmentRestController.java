package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.aspect.TrackMetric;
import by.dudkin.driver.rest.api.AssignmentApi;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.AvailableDriverResponse;
import by.dudkin.driver.util.AssignmentSpecification;
import by.dudkin.driver.service.api.AssignmentService;
import by.dudkin.driver.util.MetricUtils;
import by.dudkin.driver.util.TokenConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static by.dudkin.driver.util.TokenConstants.USERNAME_CLAIM_EXPRESSION;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
public class AssignmentRestController implements AssignmentApi {

    private final AssignmentService assignmentService;
    private final AssignmentSpecification assignmentSpecification;

    @Override
    public ResponseEntity<AssignmentResponse> get(UUID assignmentId) {
        return new ResponseEntity<>(assignmentService.findById(assignmentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginatedResponse<AssignmentResponse>> getAll(
            @RequestParam(required = false) UUID driverId,
            @RequestParam(required = false) UUID carId,
            Pageable pageable) {
        var specification = assignmentSpecification.getSpecification(driverId, carId);
        return new ResponseEntity<>(assignmentService.findAll(specification, pageable), HttpStatus.OK);
    }

    @Override
    @TrackMetric(metricName = MetricUtils.ASSIGNMENTS_CREATED_COUNT)
    public ResponseEntity<AssignmentResponse> save(AssignmentRequest assignmentRequest,
                                                   @AuthenticationPrincipal(expression = USERNAME_CLAIM_EXPRESSION) String username) {
        return new ResponseEntity<>(assignmentService.create(assignmentRequest, username), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AvailableDriverResponse> search(String username) {
        return new ResponseEntity<>(assignmentService.search(username), HttpStatus.OK);
    }

    @Override
    @TrackMetric(metricName = MetricUtils.ASSIGNMENTS_CANCELLED_COUNT)
    public ResponseEntity<AssignmentResponse> cancel(UUID assignmentId) {
        return new ResponseEntity<>(assignmentService.cancelAssignment(assignmentId), HttpStatus.OK);
    }

    @Override
    @TrackMetric(metricName = MetricUtils.ASSIGNMENTS_DELETED_COUNT)
    public ResponseEntity<Void> delete(UUID assignmentId) {
        assignmentService.delete(assignmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
