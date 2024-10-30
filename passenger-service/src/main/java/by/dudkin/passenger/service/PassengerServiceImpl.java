package by.dudkin.passenger.service;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import by.dudkin.passenger.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Locale;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final MessageSource messageSource;
    private final PassengerMapper passengerMapper;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerDto findById(long id) throws DataAccessException {
        return passengerMapper.toPassengerDto(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<PassengerDto> findAll() throws DataAccessException {
        return passengerMapper.toPassengerDtos(passengerRepository.findAll());
    }

    @Override
    public PassengerDto create(PassengerFieldsDto passengerFieldsDto) throws DataAccessException {
        Passenger passenger = passengerMapper.toPassenger(passengerFieldsDto);
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerDto(passenger);
    }

    @Override
    public PassengerDto update(long passengerId, PassengerFieldsDto passengerFieldsDto) throws DataAccessException {
        Passenger passenger = passengerMapper.toPassenger(passengerFieldsDto);
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerDto(passenger);
    }

    @Override
    public void delete(long passengerId) throws DataAccessException {
        Passenger passenger = getOrThrow(passengerId);
        passengerRepository.delete(passenger);
    }

    private Passenger getOrThrow(long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(
                        messageSource.getMessage(ErrorMessages.PASSENGER_NOT_FOUND, new Object[]{id}, Locale.getDefault())
                ));
    }

}
