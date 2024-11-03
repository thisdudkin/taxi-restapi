package by.dudkin.passenger.service;

import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface PassengerService {

    PassengerResponse findById(long id);

    Collection<PassengerResponse> findAll();

    PassengerResponse create(PassengerRequest passengerRequest);

    PassengerResponse update(long passengerId, PassengerRequest passengerRequest);

    void delete(long passengerId);

}
