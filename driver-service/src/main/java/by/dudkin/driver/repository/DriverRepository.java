package by.dudkin.driver.repository;

import by.dudkin.common.enums.DriverStatus;
import by.dudkin.driver.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Collection<Driver> findAllByStatus(DriverStatus status);
}
