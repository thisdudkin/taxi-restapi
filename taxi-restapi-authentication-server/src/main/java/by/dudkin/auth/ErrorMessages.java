package by.dudkin.auth;

/**
 * @author Alexander Dudkin
 */
final class ErrorMessages {

    private ErrorMessages() {}

    /**
     * Common Errors
     */
    public static final String GENERAL_ERROR = "error.general";

    /**
     * Authentication Server Errors
     */
    public static final String NOT_ALLOWED_ROLE = "error.auth.not.allowed.role";
    public static final String KEYCLOAK_RESPONSE_ERROR = "error.auth.keycloak.response.error";

}
