package by.dudkin.driver.repository;

import by.dudkin.driver.domain.DriverCarAssignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
public interface AssignmentRepository extends JpaRepository<DriverCarAssignment, Long> {

    @EntityGraph(attributePaths = {"driver", "car"})
    List<DriverCarAssignment> findByDriverIdAndAssignmentDateBetween(Long driverId, Instant start, Instant end);

    @EntityGraph(attributePaths = {"driver", "car"})
    List<DriverCarAssignment> findByCarId(Long carId);

}
