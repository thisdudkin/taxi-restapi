package by.dudkin.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record AccountRequest(UUID userId, BigDecimal amount) { }
