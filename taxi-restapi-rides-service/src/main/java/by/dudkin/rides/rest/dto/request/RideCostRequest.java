package by.dudkin.rides.rest.dto.request;

import by.dudkin.common.util.Location;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record RideCostRequest(Location from, Location to) implements Serializable {}
