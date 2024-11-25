package by.dudkin.passenger.service;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.common.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerMapper passengerMapper;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse findById(long id) {
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
    public PassengerResponse update(long passengerId, PassengerRequest passengerRequest) {
        Passenger passenger = getOrThrow(passengerId);
        passengerMapper.updatePassenger(passengerRequest, passenger);
        passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    @Override
    public void delete(long passengerId) {
        Passenger passenger = getOrThrow(passengerId);
        passengerRepository.delete(passenger);
    }

    Passenger getOrThrow(long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND));
    }

}
