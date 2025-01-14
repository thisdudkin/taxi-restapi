package by.dudkin.promocode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
record Promocode(UUID id, String code, int discount, LocalDateTime createdAt) {}
