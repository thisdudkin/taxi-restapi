package by.dudkin.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AccountResponse(UUID id, UUID userId, BigDecimal balance, String type) implements Serializable { }
