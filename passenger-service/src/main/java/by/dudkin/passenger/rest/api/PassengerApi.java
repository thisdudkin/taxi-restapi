package by.dudkin.passenger.rest.api;

import by.dudkin.passenger.rest.dto.request.PassengerRequest;
import by.dudkin.passenger.rest.dto.response.PassengerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexander Dudkin
 */
public interface PassengerApi {
    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Operation(
        operationId = "addPassenger",
        summary = "Create a Passenger",
        description = "Creates a passenger .",
        tags = {"passenger"},
        responses = {@ApiResponse(
            responseCode = "200",
            description = "Passenger created successfully.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = PassengerResponse.class
                )
            )}
        ), @ApiResponse(
            responseCode = "304",
            description = "Not modified."
        ), @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                mediaType = "application/json"
                // TODO: RestErrorDto
            )}
        ), @ApiResponse(
            responseCode = "500",
            description = "Server error.",
            content = {@Content(
                mediaType = "application/json"
                // TODO: RestErrorDto
            )}
        )}
    )
    @RequestMapping(
        method = {RequestMethod.POST},
        value = {"/passengers"},
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    default ResponseEntity<PassengerResponse> save(@Parameter(name = "PassengerRequest", description = "The passenger", required = true) @RequestBody @Valid PassengerRequest passengerFieldsDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
        operationId = "deletePassenger",
        summary = "Delete a passenger by ID",
        description = "Returns the passenger or a 404 error.",
        tags = {"passenger"},
        responses = {@ApiResponse(
            responseCode = "200",
            description = "Passenger details found and returned.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = PassengerResponse.class
                )
            )}
        ), @ApiResponse(
            responseCode = "304",
            description = "Not modified."
        ), @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        ), @ApiResponse(
            responseCode = "404",
            description = "Passenger not found.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        ), @ApiResponse(
            responseCode = "500",
            description = "Server error.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        )}
    )
    @RequestMapping(
        method = {RequestMethod.DELETE},
        value = {"/passengers/{passengerId}"},
        produces = {"application/json"}
    )
    default ResponseEntity<Void> deletePassenger(@Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH) @PathVariable("passengerId") @Min(0L) long passengerId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
        operationId = "getPassenger",
        summary = "Get a passenger by ID",
        description = "Returns the passenger or a 404 error.",
        tags = {"passenger"},
        responses = {@ApiResponse(
            responseCode = "200",
            description = "Passenger found and returned.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = PassengerResponse.class
                )
            )}
        ), @ApiResponse(
            responseCode = "304",
            description = "Not modified."
        ), @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        ), @ApiResponse(
            responseCode = "404",
            description = "Passenger not found.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        ), @ApiResponse(
            responseCode = "500",
            description = "Server error.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        )}
    )
    @RequestMapping(
        method = {RequestMethod.GET},
        value = {"/passengers/{passengerId}"},
        produces = {"application/json"}
    )
    default ResponseEntity<PassengerResponse> get(@Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH) @PathVariable("passengerId") @Min(0L) long passengerId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
        operationId = "listPassengers",
        summary = "Lists passengers",
        description = "Returns an array of passengers.",
        tags = {"passenger"},
        responses = {@ApiResponse(
            responseCode = "200",
            description = "Passengers found and returned.",
            content = {@Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(
                        implementation = PassengerResponse.class
                    )
                )
            )}
        ), @ApiResponse(
            responseCode = "304",
            description = "Not modified."
        ), @ApiResponse(
            responseCode = "500",
            description = "Server error.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        )}
    )
    @RequestMapping(
        method = {RequestMethod.GET},
        value = {"/passengers"},
        produces = {"application/json"}
    )
    default ResponseEntity<List<PassengerResponse>> getAll() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
        operationId = "updatePassenger",
        summary = "Update a passenger by ID",
        description = "Returns the passenger or a 404 error.",
        tags = {"passenger"},
        responses = {@ApiResponse(
            responseCode = "200",
            description = "Passenger details found and returned.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = PassengerResponse.class
                )
            )}
        ), @ApiResponse(
            responseCode = "304",
            description = "Not modified."
        ), @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        ), @ApiResponse(
            responseCode = "404",
            description = "Passenger not found.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        ), @ApiResponse(
            responseCode = "500",
            description = "Server error.",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(
                    // TODO: RestErrorDto.class
                )
            )}
        )}
    )
    @RequestMapping(
        method = {RequestMethod.PUT},
        value = {"/passengers/{passengerId}"},
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    default ResponseEntity<PassengerResponse> update(@Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH) @PathVariable("passengerId") @Min(0L) long passengerId, @Parameter(name = "PassengerResponse", description = "The passenger", required = true) @RequestBody @Valid PassengerRequest passengerRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
