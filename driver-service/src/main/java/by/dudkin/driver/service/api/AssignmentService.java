package by.dudkin.driver.service.api;

import by.dudkin.driver.domain.DriverCarAssignment;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface AssignmentService {
    AssignmentResponse create(AssignmentRequest assignmentRequest);
    AssignmentResponse findById(long assignmentId);
    Page<AssignmentResponse> findAll(Specification<DriverCarAssignment> spec, Pageable pageable);
    Page<AssignmentResponse> findAllByDriver(long driverId, Pageable pageable);
    Page<AssignmentResponse> findAllByCar(long carId, Pageable pageable);
    AssignmentResponse cancelAssignment(long assignmentId, AssignmentRequest assignmentRequest);
    void delete(long assignmentId);
}
