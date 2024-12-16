package by.dudkin.driver.rest.controller;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.rest.api.AssignmentApi;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.util.AssignmentSpecification;
import by.dudkin.driver.service.api.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
    public ResponseEntity<AssignmentResponse> save(AssignmentRequest assignmentRequest) {
        return new ResponseEntity<>(assignmentService.create(assignmentRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AssignmentResponse> cancel(UUID assignmentId) {
        return new ResponseEntity<>(assignmentService.cancelAssignment(assignmentId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(UUID assignmentId) {
        assignmentService.delete(assignmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
