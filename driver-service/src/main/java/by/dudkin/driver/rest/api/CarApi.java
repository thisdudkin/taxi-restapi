package by.dudkin.driver.rest.api;

import by.dudkin.driver.rest.dto.request.CarRequest;
import by.dudkin.driver.rest.dto.response.CarResponse;
import by.dudkin.driver.rest.dto.response.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Alexander Dudkin
 */
public interface CarApi {

    @Operation(
        operationId = "getCar",
        summary = "Get a car by ID",
        description = "Returns the car or a 404 error.",
        tags = {"car"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Car found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "404", description = "Car not found.")
        }
    )
    @GetMapping(value = "/cars/{carId}", produces = "application/json")
    ResponseEntity<CarResponse> get(@Parameter(name = "carId", description = "The ID of the car.", required = true, in = ParameterIn.PATH) @PathVariable("carId") long carId);

    @Operation(
        operationId = "listCars",
        summary = "Lists cars with pagination",
        description = "Returns a paginated list of cars.",
        tags = {"car"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cars found and returned.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CarResponse.class))
                )
            ),
            @ApiResponse(responseCode = "500", description = "Server error.")
        }
    )
    @GetMapping(value = "/cars", produces = "application/json")
    ResponseEntity<PaginatedResponse<CarResponse>> getAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(
        operationId = "addCar",
        summary = "Create a car",
        description = "Creates a new car.",
        tags = {"car"},
        responses = {
            @ApiResponse(responseCode = "201", description = "Car created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PostMapping(value = "/cars", produces = "application/json", consumes = "application/json")
    ResponseEntity<CarResponse> save(@Parameter(name = "CarRequest", description = "Car data", required = true) @RequestBody @Valid CarRequest carRequest);

    @Operation(
        operationId = "updateCar",
        summary = "Update a car by ID",
        description = "Updates car details.",
        tags = {"car"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "404", description = "Car not found.")
        }
    )
    @PutMapping(value = "/cars/{carId}", produces = "application/json", consumes = "application/json")
    ResponseEntity<CarResponse> update(
        @Parameter(name = "carId", description = "The ID of the car.", required = true, in = ParameterIn.PATH)
        @PathVariable("carId") long carId,
        @Parameter(name = "CarRequest", description = "Car data", required = true)
        @RequestBody @Valid CarRequest carRequest);

    @Operation(
        operationId = "deleteCar",
        summary = "Delete a car by ID",
        description = "Deletes a car by ID.",
        tags = {"car"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Car deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Car not found.")
        }
    )
    @DeleteMapping(value = "/cars/{carId}")
    ResponseEntity<Void> delete(@Parameter(name = "carId", description = "The ID of the car.", required = true, in = ParameterIn.PATH) @PathVariable("carId") long carId);

}
