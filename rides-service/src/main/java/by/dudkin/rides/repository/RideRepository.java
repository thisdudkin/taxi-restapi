package by.dudkin.rides.repository;

import by.dudkin.rides.domain.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alexander Dudkin
 */
public interface RideRepository extends JpaRepository<Ride, Long> {
    Page<Ride> findAll(Specification<Ride> spec, Pageable pageable);
}
