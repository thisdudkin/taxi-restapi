package by.dudkin.driver.mapper;

import by.dudkin.driver.domain.Driver;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "rating", source = "averageRating")
    DriverResponse toResponse(Driver driver);

    Driver toDriver(DriverRequest driverRequest);

    void updateDriver(DriverRequest driverRequest, @MappingTarget Driver driver);

}
