package by.dudkin.passenger.service;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.ErrorMessages;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.JdbcPassengerRepository;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.request.FeedbackRequest;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerMapper passengerMapper;
    private final PassengerRepository passengerRepository;
    private final JdbcPassengerRepository jdbcPassengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse findById(UUID id) {
        return passengerMapper.toResponse(getOrThrow(id));
    }

    @Override
    public PassengerResponse findByUsername(String username) {
        return passengerRepository.findByUsername(username)
            .map(passengerMapper::toResponse)
            .orElseThrow(() -> new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND));
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
    public PassengerResponse create(PassengerRequest passengerRequest, String username) {
        Passenger passenger = passengerMapper.toPassenger(passengerRequest);
        passenger.setUsername(username);
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
        return jdbcPassengerRepository.getBalance(passengerId);
    }

    @Override
    public void updateBalance(UUID passengerId, BigDecimal amount) {
        passengerRepository.updateBalance(passengerId, amount);
    }

    @Override
    public PassengerResponse ratePassenger(UUID passengerId, FeedbackRequest feedbackRequest) {
        passengerRepository.ratePassenger(UUID.randomUUID(), passengerId, feedbackRequest.rating());
        return passengerMapper.toResponse(getOrThrow(passengerId));
    }

    Passenger getOrThrow(UUID id) {
        return passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND));
    }

}
