package by.dudkin.driver.service.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.domain.Assignment;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface AssignmentService {
    AssignmentResponse create(AssignmentRequest assignmentRequest, String username);
    AssignmentResponse findById(UUID assignmentId);
    PaginatedResponse<AssignmentResponse> findAll(Specification<Assignment> spec, Pageable pageable);
    AssignmentResponse cancelAssignment(UUID assignmentId);
    void delete(UUID assignmentId);
}
