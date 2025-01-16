package by.dudkin.promocode;

/**
 * @author Alexander Dudkin
 */
final class TokenConstants {

    private TokenConstants() {
        throw new AssertionError();
    }

    static final String USERNAME_CLAIM = "preferred_username";
    static final String SPRING_ROLES_CLAIM = "spring_sec_roles";

}
