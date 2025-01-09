package by.dudkin.promocode;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Alexander Dudkin
 */
@Repository
class JdbcPromocodeRepository implements PromocodeRepository {

    final JdbcTemplate jdbcTemplate;

    JdbcPromocodeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Promocode createPromocode(ResultSet promocodeSet) throws SQLException {
        return new Promocode(
            promocodeSet.getObject("id", UUID.class),
            promocodeSet.getString("code"),
            promocodeSet.getInt("discount"),
            promocodeSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public void savePromocodes(Collection<Promocode> promocodes) {
        jdbcTemplate.execute("insert into promocodes(id, code, discount, created_at) values (?, ?, ?, ?)",
            (PreparedStatementCallback<?>) ps -> {
                for (Promocode promocode : promocodes) {
                    ps.setObject(1, UUID.randomUUID());
                    ps.setString(2, promocode.code());
                    ps.setInt(3, promocode.discount());
                    ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    ps.addBatch();
                }
                ps.executeBatch();
                return null;
            });
    }

    @Override
    public Optional<Promocode> getByCode(String code) {
        return jdbcTemplate.execute("select p.id, p.code, p.discount, p.created_at " +
                                    "from promocodes p " +
                                    "where code = ?", (PreparedStatementCallback<Optional<Promocode>>) selectPromocodes -> {
                selectPromocodes.setString(1, code);
                try (ResultSet promocodeSet = selectPromocodes.executeQuery()) {
                    if (promocodeSet.next()) {
                        return Optional.of(createPromocode(promocodeSet));
                    }
                    return Optional.empty();
                }
            }
        );
    }

    @Override
    public Set<Promocode> getActivePromocodes(int page, int size) {
        class SeekParams {
            LocalDateTime lastSeenDateTime;
            UUID lastSeenId;

            SeekParams(LocalDateTime dateTime, UUID id) {
                this.lastSeenDateTime = dateTime;
                this.lastSeenId = id;
            }
        }

        final SeekParams seekParams = new SeekParams(
            LocalDateTime.of(1970, 1, 1, 0, 0),
            new UUID(0L, 0L)
        );

        if (page > 0) {
            jdbcTemplate.query("select id, created_at " +
                               "from promocodes " +
                               "order by created_at, id " +
                               "limit 1 offset ?", (rs, rowNum) -> {
                    seekParams.lastSeenDateTime = rs.getTimestamp("created_at").toLocalDateTime();
                    seekParams.lastSeenId = UUID.fromString(rs.getString("id"));
                    return null;
                },
                (page * size) - 1
            );
        }

        return jdbcTemplate.execute("select id, code, discount, created_at " +
                             "from promocodes " +
                             "where (created_at, id::uuid) > (?, ?::uuid) " +
                             "order by created_at, id " +
                             "limit ?", (PreparedStatementCallback<Set<Promocode>>) selectPromocodes -> {
                selectPromocodes.setTimestamp(1, Timestamp.valueOf(seekParams.lastSeenDateTime));
                selectPromocodes.setObject(2, seekParams.lastSeenId);
                selectPromocodes.setInt(3, size);

                try (ResultSet promocodeSet = selectPromocodes.executeQuery()) {
                    Set<Promocode> promocodes = new HashSet<>();
                    while (promocodeSet.next()) {
                        promocodes.add(createPromocode(promocodeSet));
                    }
                    return promocodes;
                }
            }
        );
    }

    @Override
    public void deleteRecentPromocodes() {
        jdbcTemplate.execute("delete from promocodes where created_at >= ? and created_at <= ?",
            (PreparedStatementCallback<?>) ps -> {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDate.now().minusDays(1).atStartOfDay()));
                ps.setTimestamp(2, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
                ps.execute();
                return null;
            });
    }

}
