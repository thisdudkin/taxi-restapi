package by.dudkin.driver.repository;

import by.dudkin.driver.rest.dto.request.AvailableDriver;
import by.dudkin.driver.rest.dto.response.PendingRide;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alexander Dudkin
 */
@Repository
@RequiredArgsConstructor
public class AvailableDriverService {

    private final JdbcTemplate jdbcTemplate;

    // for Belarus only
    private static final double minLat = 51.26;
    private static final double maxLat = 56.17;
    private static final double minLng = 23.17;
    private static final double maxLng = 32.77;

    public AvailableDriver findAvailableDriver(PendingRide ride) {
        return jdbcTemplate.queryForObject("""
                select dca.driver_id, dca.car_id
                from driver_car_assignments dca
                join public.drivers d on d.id = dca.driver_id
                where d.status = 'READY' AND dca.status = 'ACTIVE'
                order by d.balance limit 1
                """,
            (rs, rowNum) -> new AvailableDriver(
                ride.rideId(),
                rs.getLong("driver_id"),
                rs.getLong("car_id"),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(minLat, maxLat)).setScale(6, RoundingMode.HALF_UP),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(minLng, maxLng)).setScale(6, RoundingMode.HALF_UP)
            ));
    }

}
