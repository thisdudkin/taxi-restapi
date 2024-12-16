package by.dudkin.notification.service.dao;

import by.dudkin.notification.domain.AvailableDriver;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
@Transactional
@AllArgsConstructor
public class DriverDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertAvailableDriver(AvailableDriver driver) {
        jdbcTemplate.execute("insert into available_drivers (id, driver_id, car_id, location) " +
            "values (?, ?, ?, st_setsrid(st_makepoint(?, ?), 4326))",
            (PreparedStatementCallback<?>) ps -> {
                ps.setObject(1, UUID.randomUUID());
                ps.setObject(2, driver.driverId());
                ps.setObject(3, driver.carId());
                ps.setBigDecimal(4, driver.lng());
                ps.setBigDecimal(5, driver.lat());
                ps.execute();
                return null;
            });
    }

    public void removeAvailableDriver(UUID driverId, UUID carId) {
        jdbcTemplate.execute("delete from available_drivers " +
            "where driver_id = ? and car_id = ?", (PreparedStatementCallback<?>) ps -> {
            ps.setObject(1, driverId);
            ps.setObject(2, carId);
            ps.execute();
            return null;
        });
    }

}
