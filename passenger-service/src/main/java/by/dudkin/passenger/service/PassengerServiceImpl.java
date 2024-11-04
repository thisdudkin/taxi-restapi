package by.dudkin.passenger.service;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import by.dudkin.common.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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
    public Collection<PassengerResponse> findAll() {
        return passengerMapper.toPassengerDtos(passengerRepository.findAll());
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
        return passengerMapper.toResponse(passenger);
    }

    @Override
    public void delete(long passengerId) {
        Passenger passenger = getOrThrow(passengerId);
        passengerRepository.delete(passenger);
    }

    private Passenger getOrThrow(long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(ErrorMessages.PASSENGER_NOT_FOUND));
    }

}
