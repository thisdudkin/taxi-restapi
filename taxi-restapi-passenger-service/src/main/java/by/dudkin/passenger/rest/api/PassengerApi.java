package by.dudkin.passenger.rest.api;

import by.dudkin.common.util.BalanceResponse;
import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.passenger.rest.dto.request.FeedbackRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RequestMapping("/api/passengers")
public interface PassengerApi {

    @Operation(
        operationId = "getPassenger",
        summary = "Get a passenger by ID",
        description = "Returns the passenger or a 404 error.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Passenger found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Passenger not found.")
        }
    )
    @GetMapping(value = "/{passengerId}", produces = "application/json")
    ResponseEntity<PassengerResponse> get(@Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH) @PathVariable("passengerId") UUID passengerId);

    @Operation(
        operationId = "getPassengerByUsername",
        summary = "Get a passenger by username",
        description = "Returns the passenger or a 404 error.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Passenger found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Passenger not found.")
        }
    )
    @GetMapping(value = "/search", produces = "application/json")
    ResponseEntity<PassengerResponse> search(
        @Parameter(name = "username", description = "The username of the passenger.", required = true, in = ParameterIn.PATH)
        @RequestParam String username
    );

    @Operation(
        operationId = "listPassengers",
        summary = "Lists passengers with pagination",
        description = "Returns a paginated list of passengers.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Passengers found and returned.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PassengerResponse.class))
                )
            ),
            @ApiResponse(responseCode = "500", description = "Server error.")
        }
    )
    @GetMapping(produces = "application/json")
    ResponseEntity<PaginatedResponse<PassengerResponse>> getAll(@Parameter(hidden = true) Pageable pageable);

    @Operation(
        operationId = "addPassenger",
        summary = "Create a passenger",
        description = "Creates a new passenger.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "201", description = "Passenger created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PostMapping(produces = "application/json", consumes = "application/json")
    ResponseEntity<PassengerResponse> save(
        @Parameter(name = "PassengerRequest", description = "Passenger data", required = true)
        @RequestBody @Valid PassengerRequest passengerRequest,
        String username
    );

    @Operation(
        operationId = "updatePassenger",
        summary = "Update a passenger by ID",
        description = "Updates passenger details.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Passenger updated successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PassengerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Passenger not found.")
        }
    )
    @PutMapping(value = "/{passengerId}", produces = "application/json", consumes = "application/json")
    ResponseEntity<PassengerResponse> update(@Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH) @PathVariable("passengerId") UUID passengerId, @Parameter(name = "PassengerRequest", description = "Passenger data", required = true) @RequestBody @Valid PassengerRequest passengerRequest);

    @Operation(
        operationId = "deletePassenger",
        summary = "Delete a passenger by ID",
        description = "Deletes a passenger by ID.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Passenger deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Passenger not found.")
        }
    )
    @DeleteMapping(value = "/{passengerId}")
    ResponseEntity<Void> delete(@Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH) @PathVariable("passengerId") UUID passengerId);

    @Operation(
        operationId = "checkPassengerBalance",
        summary = "Check passenger balance",
        description = "Returns the balance of a passenger by ID.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Balance retrieved successfully.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BalanceResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Passenger not found."
            )
        }
    )
    @GetMapping(value = "/{passengerId}/balance", produces = "application/json")
    ResponseEntity<BalanceResponse<UUID>> checkBalance(
        @Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH)
        @PathVariable("passengerId") UUID passengerId
    );

    @Operation(
        operationId = "updatePassengerBalance",
        summary = "Update passenger balance",
        description = "Updates the balance of a passenger by ID.",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Balance updated successfully."),
            @ApiResponse(responseCode = "404", description = "Passenger not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PutMapping(value = "/{passengerId}/balance", produces = "application/json")
    ResponseEntity<Void> updateBalance(
        @Parameter(name = "passengerId", description = "The ID of the passenger.", required = true, in = ParameterIn.PATH)
        @PathVariable("passengerId") UUID passengerId,
        @Parameter(name = "amount", description = "The amount to adjust the balance by.", required = true)
        @RequestParam BigDecimal amount
    );

    @Operation(
        operationId = "ratePassenger",
        summary = "Rate passenger",
        tags = {"passenger"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Passenger rated successfully."),
            @ApiResponse(responseCode = "404", description = "Passenger not found."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PostMapping(value = "/{passengerId}/rate", produces = "application/json")
    ResponseEntity<PassengerResponse> ratePassenger(
        @PathVariable UUID passengerId,
        @Valid @RequestBody FeedbackRequest feedbackRequest
    );


}
