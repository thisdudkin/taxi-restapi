package by.dudkin.driver.repository;

import by.dudkin.driver.domain.DriverCarAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface AssignmentRepository extends JpaRepository<DriverCarAssignment, UUID> {

    @EntityGraph(value = "assignment-car-driver")
    Optional<DriverCarAssignment> findWithDriverAndCarById(UUID assignmentId);

    @Query("""
            SELECT a FROM DriverCarAssignment a
            JOIN FETCH a.car c
            WHERE c.id = :carId AND a.status = 'ACTIVE'
            """)
    Optional<DriverCarAssignment> findActiveAssignmentByCarId(UUID carId);

    Page<DriverCarAssignment> findAll(Specification<DriverCarAssignment> spec, Pageable pageable);

}
