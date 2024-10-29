package by.dudkin.passenger.service;

import by.dudkin.passenger.entity.Passenger;
import by.dudkin.passenger.mapper.PassengerMapper;
import by.dudkin.passenger.repository.PassengerRepository;
import by.dudkin.passenger.rest.advice.custom.PassengerNotFoundException;
import by.dudkin.passenger.rest.dto.PassengerDto;
import by.dudkin.passenger.rest.dto.PassengerFieldsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static java.lang.String.format;

/**
 * @author Alexander Dudkin
 */
@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerMapper passengerMapper;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerDto findById(long id) throws DataAccessException {
        try {
            return passengerMapper.toPassengerDto(passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(format("Passenger not found wit ID: %s", id))));
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            // Just ignore not found exception for Jpa realization
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<PassengerDto> findAll() throws DataAccessException {
        return passengerMapper.toPassengerDtos(passengerRepository.findAll());
    }

    @Override
    @Transactional
    public PassengerDto create(PassengerFieldsDto passengerFieldsDto) throws DataAccessException {
        Passenger passenger = passengerMapper.toPassenger(passengerFieldsDto);
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerDto(passenger);
    }

    @Override
    @Transactional
    public PassengerDto update(Integer passengerId, PassengerFieldsDto passengerFieldsDto) throws DataAccessException {
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(format("Passenger with ID %s not found", passengerId)));
        passenger.setUsername(passengerFieldsDto.username());
        passenger.setEmail(passengerFieldsDto.email());
        passenger.setPassword(passengerFieldsDto.password());
        passenger.setInfo(passengerFieldsDto.info());
        passenger.setPreferredPaymentMethod(passengerFieldsDto.preferredPaymentMethod());
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerDto(passenger);
    }

    @Override
    @Transactional
    public void delete(Long passengerId) throws DataAccessException {
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(format("Passenger not found wit ID: %s", passengerId)));
        passengerRepository.delete(passenger);
    }

}
