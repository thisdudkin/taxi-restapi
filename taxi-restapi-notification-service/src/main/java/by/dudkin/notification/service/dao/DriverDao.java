package by.dudkin.notification.service.dao;

import by.dudkin.notification.domain.AvailableDriver;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

/**
 * @author Alexander Dudkin
 */
@Repository
@AllArgsConstructor
public class DriverDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertAvailableDriver(AvailableDriver driver) {
        jdbcTemplate.execute("insert into available_drivers (driver_id, car_id, location) " +
            "values (?, ?, st_setsrid(st_makepoint(?, ?), 4326))",
            (PreparedStatementCallback<?>) ps -> {
                ps.setLong(1, driver.driverId());
                ps.setLong(2, driver.carId());
                ps.setBigDecimal(3, driver.lng());
                ps.setBigDecimal(4, driver.lat());
                ps.execute();
                return null;
            });
    }

}
