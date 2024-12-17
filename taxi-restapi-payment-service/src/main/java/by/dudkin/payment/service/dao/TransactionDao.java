package by.dudkin.payment.service.dao;

import by.dudkin.common.util.TransactionRequest;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
@AllArgsConstructor
public class TransactionDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertTransaction(TransactionRequest<UUID> transactionRequest) throws SQLException {
        jdbcTemplate.execute("insert into transactions(id, driver_id, passenger_id, amount, created_at, ride_id) values (?, ?, ?, ?, ?, ?)",
            (PreparedStatementCallback<?>) ps -> {
                ps.setObject(1, UUID.randomUUID());
                ps.setObject(2, transactionRequest.driverId());
                ps.setObject(3, transactionRequest.passengerId());
                ps.setBigDecimal(4, transactionRequest.amount());
                ps.setObject(5, LocalDateTime.now());
                ps.setObject(6, transactionRequest.rideId());
                ps.execute();
                return null;
            });
    }

}
