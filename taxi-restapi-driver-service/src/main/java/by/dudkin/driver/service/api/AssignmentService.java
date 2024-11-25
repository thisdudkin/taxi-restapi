package by.dudkin.driver.service.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.DriverCarAssignment;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Alexander Dudkin
 */
public interface AssignmentService {
    AssignmentResponse create(AssignmentRequest assignmentRequest);

    AssignmentResponse findById(long assignmentId);

    PaginatedResponse<AssignmentResponse> findAll(Specification<DriverCarAssignment> spec, Pageable pageable);

    AssignmentResponse cancelAssignment(long assignmentId);

    void delete(long assignmentId);
}
