package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Assignment;
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
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    @EntityGraph(value = "assignment-car-driver")
    Optional<Assignment> findWithDriverAndCarById(UUID assignmentId);

    @Query("""
            SELECT a FROM Assignment a
            JOIN FETCH a.car c
            WHERE c.id = :carId AND a.status = 'ACTIVE'
            """)
    Optional<Assignment> findActiveAssignmentByCarId(UUID carId);

    Page<Assignment> findAll(Specification<Assignment> spec, Pageable pageable);

}
