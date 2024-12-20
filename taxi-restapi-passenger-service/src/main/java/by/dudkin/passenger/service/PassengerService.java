package by.dudkin.passenger.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface PassengerService {

    PassengerResponse findById(UUID id);

    PaginatedResponse<PassengerResponse> findAll(Pageable pageable);

    PassengerResponse create(PassengerRequest passengerRequest);

    PassengerResponse update(UUID passengerId, PassengerRequest passengerRequest);

    void delete(UUID passengerId);

    BalanceResponse<UUID> checkBalance(UUID passengerId);

    void updateBalance(UUID passengerId, BigDecimal amount);

}
