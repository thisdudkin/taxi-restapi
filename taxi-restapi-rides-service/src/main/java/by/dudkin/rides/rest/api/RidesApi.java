package by.dudkin.rides.rest.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.rides.rest.dto.request.RideCompletionRequest;
import by.dudkin.rides.rest.dto.request.RideCostRequest;
import by.dudkin.rides.rest.dto.request.RideRequest;
import by.dudkin.rides.rest.dto.response.AvailableDriver;
import by.dudkin.rides.rest.dto.request.PendingRide;
import by.dudkin.rides.rest.dto.response.RideCostResponse;
import by.dudkin.rides.rest.dto.response.RideResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RequestMapping("/api")
public interface RidesApi {

    @Operation(
        operationId = "getRide",
        summary = "Get a ride by ID",
        description = "Returns the ride or a 404 error.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @GetMapping(value = "/rides/{rideId}", produces = "application/json")
    ResponseEntity<RideResponse> get(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId
    );

    @Operation(
        operationId = "listRides",
        summary = "Lists rides with pagination",
        description = "Returns a paginated list of rides, with optional filtering by passenger, driver or car.",
        tags = {"ride"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rides found and returned.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RideResponse.class))
                )
            ),
            @ApiResponse(responseCode = "500", description = "Server error.")
        }
    )
    @GetMapping(value = "/rides", produces = "application/json")
    ResponseEntity<PaginatedResponse<RideResponse>> getAll(
        @Parameter(name = "passengerId", description = "Filter rides by passenger ID", required = false)
        @RequestParam(required = false) UUID passengerId,
        @Parameter(name = "driverId", description = "Filter rides by driver ID", required = false)
        @RequestParam(required = false) UUID driverId,
        @Parameter(name = "carId", description = "Filter rides by car ID", required = false)
        @RequestParam(required = false) UUID carId,
        @Parameter(hidden = true) Pageable pageable
    );

    @Operation(
        operationId = "addRide",
        summary = "Create a ride",
        description = "Creates a new ride.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "201", description = "Ride created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PostMapping(value = "/rides", produces = "application/json", consumes = "application/json")
    ResponseEntity<RideResponse> save(
        @Parameter(name = "RideRequest", description = "Ride data", required = true)
        @RequestBody @Valid RideRequest rideRequest,
        String username
    );

    @Operation(
        operationId = "updateRide",
        summary = "Update a ride by ID",
        description = "Updates ride details.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @PutMapping(value = "/rides/{rideId}", produces = "application/json", consumes = "application/json")
    ResponseEntity<RideResponse> update(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId,
        @Parameter(name = "RideRequest", description = "Ride data", required = true)
        @RequestBody @Valid RideRequest rideRequest
    );

    @Operation(
        operationId = "deleteRide",
        summary = "Delete a ride by ID",
        description = "Deletes a ride by ID.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Ride deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @DeleteMapping(value = "/rides/{rideId}")
    ResponseEntity<Void> delete(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId
    );

    @Operation(
        operationId = "activateRide",
        summary = "Mark a ride as active",
        description = "Updates the status of a ride to 'ACTIVE'.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride status updated to ACTIVE.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @PatchMapping(value = "/rides/{rideId}/activate", produces = "application/json")
    ResponseEntity<RideResponse> activate(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId
    );

    @Operation(
        operationId = "assignDriver",
        summary = "Assign a driver to the ride.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Driver has been assigned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @PostMapping(value = "/rides/{rideId}/assign", produces = "application/json")
    ResponseEntity<RideResponse> assignDriver(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId,
        String username
    );


    @Operation(
        operationId = "markRideDone",
        summary = "Mark a ride as done",
        description = "Updates the status of a ride to 'DONE'.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride status updated to DONE.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @PatchMapping(value = "/rides/{rideId}/done", produces = "application/json")
    ResponseEntity<RideResponse> markDone(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId
    );

    @Operation(
        operationId = "cancelRide",
        summary = "Cancel a ride",
        description = "Updates the status of a ride to 'CANCELLED'.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride status updated to CANCELLED.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @PatchMapping(value = "/rides/{rideId}/cancel", produces = "application/json")
    ResponseEntity<RideResponse> cancel(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId
    );

    @Operation(
        operationId = "rateRide",
        summary = "Rate a ride",
        description = "Submit a rating for a ride.",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride rated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found.")
        }
    )
    @PatchMapping(value = "/rides/{rideId}/rate", produces = "application/json", consumes = "application/json")
    ResponseEntity<RideResponse> rate(
        @Parameter(name = "rideId", description = "The ID of the ride.", required = true, in = ParameterIn.PATH)
        @PathVariable("rideId") UUID rideId,
        @Parameter(name = "RideCompletionRequest", description = "Rating details", required = true)
        @RequestBody @Valid RideCompletionRequest request
    );

    @Operation(
        operationId = "checkRideCost",
        summary = "Check the cost of a ride",
        tags = {"ride"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Ride cost info.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RideCostResponse.class))),
        }
    )
    @PostMapping(value = "/rides/cost", produces = "application/json", consumes = "application/json")
    ResponseEntity<RideCostResponse> checkRideCost(
        @Parameter(name = "RideCostRequest", required = true)
        @RequestBody @Valid RideCostRequest request
    );

}
