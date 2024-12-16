package by.dudkin.driver.repository;

import by.dudkin.driver.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface CarRepository extends JpaRepository<Car, UUID> {

    @EntityGraph(value = "car-assignments-drivers")
    Optional<Car> findWithAssignmentsAndDriversById(UUID id);

    boolean existsByLicensePlate(String licensePlate);

}
