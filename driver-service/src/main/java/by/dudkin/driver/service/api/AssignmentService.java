package by.dudkin.driver.service.api;

import by.dudkin.driver.domain.DriverCarAssignment;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.PaginatedResponse;
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

    PaginatedResponse<AssignmentResponse> findAll(Specification<DriverCarAssignment> spec, Pageable pageable);

    PaginatedResponse<AssignmentResponse> findAllByDriver(long driverId, Pageable pageable);

    PaginatedResponse<AssignmentResponse> findAllByCar(long carId, Pageable pageable);

    AssignmentResponse cancelAssignment(long assignmentId);

    void delete(long assignmentId);
}
