package by.dudkin.common.util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record TransactionRequest<ID extends Serializable>(ID driverId, ID passengerId, ID rideId, BigDecimal amount) implements Serializable {}
