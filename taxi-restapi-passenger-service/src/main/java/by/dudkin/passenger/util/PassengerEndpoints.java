package by.dudkin.passenger.util;

import by.dudkin.common.util.Endpoints;

/**
 * @author Alexander Dudkin
 */
public enum PassengerEndpoints implements Endpoints {

    BASE_URI("/api/passengers"),

    GET_PASSENGER("/api/passengers/{passengerId}"),

    SEARCH_PASSENGER("/api/passengers/username"),

    LIST_PASSENGERS("/api/passengers"),

    SAVE_PASSENGER("/api/passengers"),

    UPDATE_PASSENGER("/api/passengers/{passengerId}"),

    DELETE_PASSENGER("/api/passengers/{passengerId}"),

    CHECK_BALANCE("/api/passengers/{passengerId}/balance"),

    UPDATE_BALANCE("/api/passengers/{passengerId}/balance");

    private final String uri;

    PassengerEndpoints(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }

}
