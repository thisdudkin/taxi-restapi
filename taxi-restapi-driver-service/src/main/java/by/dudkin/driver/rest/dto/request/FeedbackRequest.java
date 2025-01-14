package by.dudkin.driver.rest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
public record FeedbackRequest(@Min(1) @Max(10) int rating) implements Serializable {}
