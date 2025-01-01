package by.dudkin.rides.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Promocode(UUID id, String code, int discount, LocalDateTime createdAt) {}
