package by.dudkin.driver.mapper;

import by.dudkin.driver.domain.Car;
import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = "spring")
public interface CarMapper {

    CarResponse toResponse(Car car);

    Car toCar(CarRequest carRequest);

    void updateCar(CarRequest carRequest, @MappingTarget Car car);

}
