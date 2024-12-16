package by.dudkin.common.util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alexander Dudkin
 */
public record BalanceResponse<ID extends Serializable>(ID id, BigDecimal amount) implements Serializable {}
