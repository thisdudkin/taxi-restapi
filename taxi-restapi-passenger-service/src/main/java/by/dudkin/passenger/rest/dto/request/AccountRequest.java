package by.dudkin.passenger.rest.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AccountRequest(UUID userId, BigDecimal amount) { }
