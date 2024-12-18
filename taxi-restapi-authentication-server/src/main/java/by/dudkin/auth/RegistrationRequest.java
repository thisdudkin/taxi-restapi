package by.dudkin.auth;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
record RegistrationRequest(String username, String email, String password, Role role) implements Serializable {}
