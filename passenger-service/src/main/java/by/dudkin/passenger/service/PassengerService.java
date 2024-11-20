package by.dudkin.passenger.service;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * @author Alexander Dudkin
 */
public interface PassengerService {

    PassengerResponse findById(long id);

    PaginatedResponse<PassengerResponse> findAll(Pageable pageable);

    PassengerResponse create(PassengerRequest passengerRequest);

    PassengerResponse update(long passengerId, PassengerRequest passengerRequest);

    void delete(long passengerId);

}
