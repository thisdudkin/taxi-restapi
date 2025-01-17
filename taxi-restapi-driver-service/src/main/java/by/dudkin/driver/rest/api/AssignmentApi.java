package by.dudkin.driver.rest.api;

import by.dudkin.common.util.PaginatedResponse;
import by.dudkin.driver.rest.dto.request.AssignmentRequest;
import by.dudkin.driver.rest.dto.response.AssignmentResponse;
import by.dudkin.driver.rest.dto.response.AvailableDriverResponse;
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

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RequestMapping("/api")
public interface AssignmentApi {

    @Operation(
        operationId = "getAssignment",
        summary = "Get an assignment by ID",
        description = "Returns the assignment or a 404 error.",
        tags = {"assignment"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Assignment found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found.")
        }
    )
    @GetMapping(value = "/assignments/{assignmentId}", produces = "application/json")
    ResponseEntity<AssignmentResponse> get(
        @Parameter(name = "assignmentId", description = "The ID of the assignment.", required = true, in = ParameterIn.PATH)
        @PathVariable("assignmentId") UUID assignmentId);

    @Operation(
        operationId = "listAssignments",
        summary = "Lists assignments with pagination",
        description = "Returns a paginated list of assignments, with optional filtering by driver or car.",
        tags = {"assignment"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Assignments found and returned.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                )
            ),
            @ApiResponse(responseCode = "500", description = "Server error.")
        }
    )
    @GetMapping(value = "/assignments", produces = "application/json")
    ResponseEntity<PaginatedResponse<AssignmentResponse>> getAll(
        @Parameter(name = "driverId", description = "Filter assignments by driver ID", required = false)
        @RequestParam(required = false) UUID driverId,
        @Parameter(name = "carId", description = "Filter assignments by car ID", required = false)
        @RequestParam(required = false) UUID carId,
        @Parameter(hidden = true) Pageable pageable);

    @Operation(
        operationId = "addAssignment",
        summary = "Create an assignment",
        description = "Creates a new assignment.",
        tags = {"assignment"},
        responses = {
            @ApiResponse(responseCode = "201", description = "Assignment created successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        }
    )
    @PostMapping(value = "/assignments", produces = "application/json", consumes = "application/json")
    ResponseEntity<AssignmentResponse> save(
        @Parameter(name = "AssignmentRequest", description = "Assignment data", required = true)
        @RequestBody @Valid AssignmentRequest assignmentRequest,
        String username
    );

    @Operation(
        operationId = "getByUsername",
        summary = "Get an assignment by username",
        description = "Returns assignment or 404 error.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Assignment found and returned.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found.")
        }
    )
    @GetMapping(value = "/assignments/search", produces = "application/json")
    ResponseEntity<AvailableDriverResponse> search(
        @Parameter(name = "username", description = "The username of the driver.", required = true, in = ParameterIn.PATH)
        @RequestParam String username
    );

    @Operation(
        operationId = "cancelAssignment",
        summary = "Cancel an assignment by ID",
        description = "Cancel assignment.",
        tags = {"assignment"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Assignment cancelled successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found.")
        }
    )
    @PutMapping(value = "/assignments/{assignmentId}", produces = "application/json")
    ResponseEntity<AssignmentResponse> cancel(
        @Parameter(name = "assignmentId", description = "The ID of the assignment.", required = true, in = ParameterIn.PATH)
        @PathVariable("assignmentId") UUID assignmentId
    );

    @Operation(
        operationId = "deleteAssignment",
        summary = "Delete an assignment by ID",
        description = "Deletes an assignment by ID.",
        tags = {"assignment"},
        responses = {
            @ApiResponse(responseCode = "204", description = "Assignment deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Assignment not found.")
        }
    )
    @DeleteMapping(value = "/assignments/{assignmentId}")
    ResponseEntity<Void> delete(
        @Parameter(name = "assignmentId", description = "The ID of the assignment.", required = true, in = ParameterIn.PATH)
        @PathVariable("assignmentId") UUID assignmentId);

}
