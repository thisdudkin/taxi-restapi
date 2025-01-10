package by.dudkin.promocode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@RequestMapping(PromocodeApi.URI)
interface PromocodeApi {
    String URI = "/api/promocodes";

    @Operation(summary = "Validate a promocode",
        description = "Checks if a promocode exists and returns its discount value if valid")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Promocode is valid",
            content = @Content(schema = @Schema(implementation = Promocode.class))),
        @ApiResponse(responseCode = "404", description = "Promocode not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    ResponseEntity<Promocode> validatePromocode(
        @Parameter(description = "Promocode to validate", required = true)
        @RequestParam String code
    );

    @Operation(summary = "Get active promocodes",
        description = "Retrieves all currently active promocodes. Requires admin privileges")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of active promocodes retrieved",
            content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/active")
    ResponseEntity<Set<Promocode>> getActivePromocodes(
        @Parameter(description = "Page number")
        @RequestParam(defaultValue = "0") int page,

        @Parameter(description = "Number of items per page")
        @RequestParam(defaultValue = "20") int size
    );

    @Operation(summary = "Delete expired promocodes",
        description = "Manually triggers deletion of expired promocodes. Requires admin privileges")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Expired promocodes deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/expired")
    ResponseEntity<Void> deleteExpiredPromocodes();

}
