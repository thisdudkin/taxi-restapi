package by.dudkin.driver.util;

import by.dudkin.common.util.Endpoints;

/**
 * @author Alexander Dudkin
 */
public enum AssignmentEndpoints implements Endpoints {

    BASE_URI("/api/assignments"),

    SAVE_ASSIGNMENT("/api/assignments"),

    GET_ASSIGNMENT_BY_ID("/api/assignments/{assignmentId}"),

    UPDATE_ASSIGNMENT("/api/assignments/{assignmentId}"),

    DELETE_ASSIGNMENT("/api/assignments/{assignmentId}");

    private final String uri;

    AssignmentEndpoints(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
