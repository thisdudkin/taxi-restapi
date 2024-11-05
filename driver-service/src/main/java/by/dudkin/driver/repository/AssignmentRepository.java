package by.dudkin.driver.repository;

import by.dudkin.driver.domain.DriverCarAssignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
    List<DriverCarAssignment> findByCarId(Long carId);

    @EntityGraph(value = "assignment-car-driver")
    Optional<DriverCarAssignment> findAssignmentByCarId(Long carId);

}
