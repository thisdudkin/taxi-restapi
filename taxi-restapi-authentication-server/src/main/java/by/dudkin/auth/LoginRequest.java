package by.dudkin.auth;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
record LoginRequest(
    @NotEmpty
    String username,
    @NotEmpty
    String password
) implements Serializable { }
