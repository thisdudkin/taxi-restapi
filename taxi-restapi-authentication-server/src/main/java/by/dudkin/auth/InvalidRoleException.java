package by.dudkin.auth;

class InvalidRoleException extends RuntimeException {
    InvalidRoleException(String message) {
        super(message);
    }
}
