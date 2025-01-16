package by.dudkin.rides.utils;

/**
 * @author Alexander Dudkin
 */
public final class TokenConstants {

    private TokenConstants() {
        throw new AssertionError();
    }

    public static final String USERNAME_CLAIM = "preferred_username";
    public static final String USERNAME_CLAIM_EXPRESSION = "claims['preferred_username']";

}
