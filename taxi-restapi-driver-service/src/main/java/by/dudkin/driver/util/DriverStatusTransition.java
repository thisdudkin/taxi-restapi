package by.dudkin.driver.util;

import by.dudkin.common.enums.DriverStatus;

/**
 * @author Alexander Dudkin
 */
public record DriverStatusTransition(DriverStatus current, DriverStatus next) {}
