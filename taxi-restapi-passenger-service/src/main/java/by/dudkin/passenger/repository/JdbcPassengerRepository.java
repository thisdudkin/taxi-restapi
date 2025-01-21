package by.dudkin.passenger.repository;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Alexander Dudkin
 */
@Repository
public class JdbcPassengerRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPassengerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BalanceResponse<UUID> getBalance(UUID passengerId) {
        return jdbcTemplate.execute(
            "select balance from passengers where id = ?",
            (PreparedStatementCallback<BalanceResponse<UUID>>) ps -> {
                ps.setObject(1, passengerId);

                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        BigDecimal balance = resultSet.getBigDecimal("balance");
                        return new BalanceResponse<>(passengerId, balance);
                    } else {
                        throw new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND);
                    }
                }
            }
        );
    }

}
