package by.dudkin.payment.service.dao;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.payment.dto.AccountRequest;
import by.dudkin.payment.dto.UserType;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Repository
@AllArgsConstructor
public class AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertAccount(AccountRequest accountRequest, UserType userType) throws SQLException {
        String sql = "INSERT INTO accounts(id, user_id, balance, type) VALUES (?, ?, ?, ?)";

        jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setObject(1, UUID.randomUUID());
            ps.setObject(2, accountRequest.userId());
            ps.setBigDecimal(3, accountRequest.amount());
            ps.setString(4, userType.name());
            return ps.execute();
        });
    }

    public BalanceResponse<UUID> getBalance(UUID id) throws SQLException {
        String sql = """
            SELECT a.user_id, a.balance
            FROM accounts a
            WHERE a.user_id = ?
            """;

        return jdbcTemplate.execute(sql, (PreparedStatementCallback<BalanceResponse<UUID>>) ps -> {
            ps.setObject(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return createResponse(resultSet);
                }

                return null;
            }
        });
    }

    public void topUpBalance(UUID userId, BigDecimal amount) {
        String sql = """
            UPDATE accounts
            SET balance = balance + ?
            WHERE user_id = ?
            """;

        jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setBigDecimal(1, amount);
            ps.setObject(2, userId);
            return ps.execute();
        });
    }

    public void decreaseBalance(UUID userId, BigDecimal amount) {
        String sql = """
            UPDATE accounts
            SET balance = balance - ?
            WHERE user_id = ?
            """;

        jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setBigDecimal(1, amount);
            ps.setObject(2, userId);
            return ps.execute();
        });
    }

    private BalanceResponse<UUID> createResponse(ResultSet resultSet) throws SQLException {
        return new BalanceResponse<>(
            resultSet.getObject("user_id", UUID.class),
            resultSet.getBigDecimal("balance")
        );
    }

}
