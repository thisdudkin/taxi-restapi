package by.dudkin.passenger.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final JdbcTemplate jdbcTemplate;
    private final PassengerMapper passengerMapper;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse findById(UUID id) {
        return passengerMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<PassengerResponse> findAll(Pageable pageable) {
        Page<Passenger> passengerPage = passengerRepository.findAll(pageable);
        List<PassengerResponse> responseList = passengerPage.getContent().stream()
            .map(passengerMapper::toResponse)
            .toList();

        return PaginatedResponse.fromPage(passengerPage, responseList);
    }

    @Override
    public PassengerResponse create(PassengerRequest passengerRequest) {
        Passenger passenger = passengerMapper.toPassenger(passengerRequest);
        passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    @Override
    public PassengerResponse update(UUID passengerId, PassengerRequest passengerRequest) {
        Passenger passenger = getOrThrow(passengerId);
        passengerMapper.updatePassenger(passengerRequest, passenger);
        passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    @Override
    public void delete(UUID passengerId) {
        Passenger passenger = getOrThrow(passengerId);
        passengerRepository.delete(passenger);
    }

    @Override
    public BalanceResponse<UUID> checkBalance(UUID passengerId) {
        final AtomicReference<BigDecimal> balance = new AtomicReference<>();
        jdbcTemplate.execute("select balance from passengers " +
            "where id = ?", (PreparedStatementCallback<?>) ps -> {
            ps.setObject(1, passengerId);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    balance.set(resultSet.getBigDecimal("balance"));
                }
            }
            return null;
        });

        if (balance.get() == null) {
            throw new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND);
        }

        return new BalanceResponse<>(passengerId, balance.get());
    }

    @Override
    public void updateBalance(UUID passengerId, BigDecimal amount) {
        jdbcTemplate.execute("update passengers set balance = balance - ? " +
            "where id = ?", (PreparedStatementCallback<?>) updatePassenger -> {
            updatePassenger.setBigDecimal(1, amount);
            updatePassenger.setObject(2, passengerId);
            updatePassenger.execute();
            return null;
        });
    }

    Passenger getOrThrow(UUID id) {
        return passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND));
    }

}
