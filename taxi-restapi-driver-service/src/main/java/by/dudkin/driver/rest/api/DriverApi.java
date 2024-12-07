package by.dudkin.driver.rest.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.rest.dto.request.DriverRequest;
import by.dudkin.driver.rest.dto.response.DriverResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alexander Dudkin
 */
@RequestMapping("/api")
public interface DriverApi {

    @Operation(
        operationId = "getDriver",
        summary = "Get a driver by ID",
        description = "Returns the driver or a 404 error.",
        tags = {"driver"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Driver found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponse.class))),
            @ApiResponse(responseCode = "404", description = "Driver not found.")
        }
    )
    @GetMapping(value = "/drivers/{driverId}", produces = "application/json")
    ResponseEntity<DriverResponse> get(@Parameter(name = "driverId", description = "The ID of the driver.", required = true, in = ParameterIn.PATH) @PathVariable("driverId") long driverId);

    @Operation(
        operationId = "listDrivers",
        summary = "Lists drivers with pagination",
        description = "Returns a paginated list of drivers.",
        tags = {"driver"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Drivers found and returned.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DriverResponse.class))
                )
            ),
            @ApiResponse(responseCode = "500", description = "Server error.")
        }
    )
    @GetMapping(value = "/drivers", produces = "application/json")
    ResponseEntity<PaginatedResponse<DriverResponse>> getAll(@Parameter(hidden = true) Pageable pageable);


    @Operation(
        operationId = "addDriver",
        summary = "Create a driver",
        description = "Creates a new driver.",
        tags = {"driver"},
        responses = {
            @ApiResponse(responseCode = "201", description = "Driver created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PostMapping(value = "/drivers", produces = "application/json", consumes = "application/json")
    ResponseEntity<DriverResponse> save(@Parameter(name = "DriverRequest", description = "Driver data", required = true) @RequestBody @Valid DriverRequest driverRequest);

    @Operation(
        operationId = "updateDriver",
        summary = "Update a driver by ID",
        description = "Updates driver details.",
        tags = {"driver"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Driver updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponse.class))),
            @ApiResponse(responseCode = "404", description = "Driver not found.")
        }
    )
    @PutMapping(value = "/drivers/{driverId}", produces = "application/json", consumes = "application/json")
    ResponseEntity<DriverResponse> update(@Parameter(name = "driverId", description = "The ID of the driver.", required = true, in = ParameterIn.PATH) @PathVariable("driverId") long driverId, @Parameter(name = "DriverRequest", description = "Driver data", required = true) @RequestBody @Valid DriverRequest driverRequest);

    @Operation(
        operationId = "deleteDriver",
        summary = "Delete a driver by ID",
        description = "Deletes a driver by ID.",
        tags = {"driver"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Driver deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Driver not found.")
        }
    )
    @DeleteMapping(value = "/drivers/{driverId}")
    ResponseEntity<Void> delete(@Parameter(name = "driverId", description = "The ID of the driver.", required = true, in = ParameterIn.PATH) @PathVariable("driverId") long driverId);

    @PutMapping(value = "/drivers/{driverId}/status/available")
    @Operation(
        operationId = "markDriverAsAvailable",
        summary = "Mark a driver as available",
        description = "Sets the driver's status to READY, indicating that the driver is available for new rides.",
        tags = {"driver"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Driver status updated to READY successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Driver not found."),
            @ApiResponse(responseCode = "400", description = "Invalid state transition.")
        }
    )
    ResponseEntity<DriverResponse> markAvailable(@Parameter(name = "driverId", description = "The ID of the driver.", required = true, in = ParameterIn.PATH) @PathVariable long driverId);

    @PutMapping(value = "/drivers/{driverId}/status/busy")
    @Operation(
        operationId = "markDriverAsBusy",
        summary = "Mark a driver as busy",
        description = "Sets the driver's status to ON_TRIP, indicating that the driver is currently on a trip.",
        tags = {"driver"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Driver status updated to ON_TRIP successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Driver not found."),
            @ApiResponse(responseCode = "400", description = "Invalid state transition.")
        }
    )
    ResponseEntity<DriverResponse> markOnTrip(@Parameter(name = "driverId", description = "The ID of the driver.", required = true, in = ParameterIn.PATH) @PathVariable long driverId);

    @PutMapping(value = "/drivers/{driverId}/status/offline")
    @Operation(
        operationId = "markDriverAsOffline",
        summary = "Mark a driver as offline",
        description = "Sets the driver's status to OFFLINE, indicating that the driver is not available for rides.",
        tags = {"driver"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Driver status updated to OFFLINE successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = DriverResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Driver not found."),
            @ApiResponse(responseCode = "400", description = "Invalid state transition.")
        }
    )
    ResponseEntity<DriverResponse> markOffline(@Parameter(name = "driverId", description = "The ID of the driver.", required = true, in = ParameterIn.PATH) @PathVariable long driverId);

}
