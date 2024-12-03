package by.dudkin.rides.repository;

import by.dudkin.common.util.Location;
import by.dudkin.rides.rest.dto.request.PendingRide;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Repository
@RequiredArgsConstructor
public class PendingRideService {

    private final JdbcTemplate jdbcTemplate;

    public Page<PendingRide> findAllPendingRides(Pageable pageable) {
        List<PendingRide> rides = jdbcTemplate.query("""
                select id, from_country, from_city, from_street, from_lat, from_lng,
                       to_country, to_city, to_street, to_lat, to_lng, price
                from rides
                where status = 'PENDING'
                limit ? offset ?
                """,
            ps -> {
                ps.setInt(1, pageable.getPageSize());
                ps.setInt(2, (int) pageable.getOffset());
            },
            (rs, rowNum) -> new PendingRide(
                rs.getLong("id"),
                new Location(
                    rs.getString("from_country"),
                    rs.getString("from_city"),
                    rs.getString("from_street"),
                    rs.getDouble("from_lat"),
                    rs.getDouble("from_lng")
                ),
                new Location(
                    rs.getString("to_country"),
                    rs.getString("to_city"),
                    rs.getString("to_street"),
                    rs.getDouble("to_lat"),
                    rs.getDouble("to_lng")
                ),
                rs.getBigDecimal("price")
            )
        );

        Long total = jdbcTemplate.queryForObject("select count(*) from rides where status = 'PENDING'", Long.class);
        return new PageImpl<>(rides, pageable, total);
    }

}
