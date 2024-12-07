package by.dudkin.rides.utils;

import by.dudkin.common.enums.RideStatus;

/**
 * @author Alexander Dudkin
 */
public record RideStatusTransition(RideStatus current, RideStatus next) {}
