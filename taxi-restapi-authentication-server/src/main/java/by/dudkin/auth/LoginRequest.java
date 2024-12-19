package by.dudkin.auth;

import java.io.Serializable;

/**
 * @author Alexander Dudkin
 */
record LoginRequest(String username, String password) implements Serializable {}
