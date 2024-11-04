package by.dudkin.driver.mapper;

import by.dudkin.driver.domain.DriverCarAssignment;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = "spring", uses = {DriverMapper.class, CarMapper.class})
public interface AssignmentMapper {

    AssignmentResponse toResponse(DriverCarAssignment assignment);

    DriverCarAssignment toAssignment(AssignmentRequest assignmentRequest);

    void updateAssignment(AssignmentRequest assignmentRequest, @MappingTarget DriverCarAssignment assignment);

}
