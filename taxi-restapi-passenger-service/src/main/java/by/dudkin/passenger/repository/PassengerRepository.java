package by.dudkin.passenger.repository;

import by.dudkin.passenger.entity.Passenger;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
    Optional<Passenger> findByUsername(String username);

    @Modifying
    @Query(value = "insert into passenger_ratings (id, passenger_id, rating) values (:id, :passengerId, :rating)", nativeQuery = true)
    void ratePassenger(UUID id, UUID passengerId, int rating);

}
