package by.dudkin.payment.service.dao;

import by.dudkin.common.util.TransactionRequest;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Alexander Dudkin
 */
@Repository
@AllArgsConstructor
public class TransactionDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertTransaction(TransactionRequest<Long> transactionRequest) throws SQLException {
        jdbcTemplate.execute("insert into transactions(driver_id, passenger_id, amount, created_at, ride_id) values (?, ?, ?, ?, ?)",
            (PreparedStatementCallback<?>) ps -> {
                ps.setLong(1, transactionRequest.driverId());
                ps.setLong(2, transactionRequest.passengerId());
                ps.setBigDecimal(3, transactionRequest.amount());
                ps.setObject(4, LocalDateTime.now());
                ps.setLong(5, transactionRequest.rideId());
                ps.execute();
                return null;
            });
    }

}
