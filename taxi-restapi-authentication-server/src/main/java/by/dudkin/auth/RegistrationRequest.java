package by.dudkin.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
record RegistrationRequest(
    @NotEmpty
    String username,
    @Email
    @NotEmpty
    String email,
    @Min(8)
    @Max(128)
    @NotEmpty
    String password,
    @NotNull
    Role role
) implements Serializable { }
