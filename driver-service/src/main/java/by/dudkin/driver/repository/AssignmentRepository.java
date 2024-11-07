package by.dudkin.driver.repository;

import by.dudkin.driver.domain.DriverCarAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
public interface AssignmentRepository extends JpaRepository<DriverCarAssignment, Long> {

    @EntityGraph(value = "assignment-car-driver")
    List<DriverCarAssignment> findByDriverIdAndAssignmentDateBetween(Long driverId, Instant start, Instant end);

    @EntityGraph(value = "assignment-car-driver")
    Page<DriverCarAssignment> findByCarId(long carId, Pageable pageable);

    @EntityGraph(value = "assignment-car-driver")
    List<DriverCarAssignment> findAllByCarId(Long carId);

    @EntityGraph(value = "assignment-car-driver")
    Page<DriverCarAssignment> findByDriverId(long driverId, Pageable pageable);

    @EntityGraph(value = "assignment-car-driver")
    Optional<DriverCarAssignment> findByCarId(Long carId);

    @EntityGraph(value = "assignment-car-driver")
    Optional<DriverCarAssignment> findWithDriverAndCarById(long assignmentId);

    @EntityGraph(value = "assignment-car-driver")
    List<DriverCarAssignment> findByDriverIdAndCarId(long driverId, long carId);

    @Query("""
            SELECT a FROM DriverCarAssignment a
            JOIN FETCH a.car c
            WHERE c.id = :carId AND a.status = 'ACTIVE'
            """)
    Optional<DriverCarAssignment> findActiveAssignmentByCarId(long carId);

    @Query("""
        SELECT a FROM DriverCarAssignment a
        JOIN FETCH a.driver d
        JOIN FETCH a.car c
        WHERE (:spec IS NULL OR a = :spec)
        """)
    Page<DriverCarAssignment> findAllWithDriverAndCar(@Nullable Specification<DriverCarAssignment> spec, Pageable pageable);

}
