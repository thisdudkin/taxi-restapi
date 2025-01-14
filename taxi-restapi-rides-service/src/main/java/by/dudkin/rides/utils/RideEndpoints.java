package by.dudkin.rides.utils;

import by.dudkin.common.util.Endpoints;

/**
 * @author Alexander Dudkin
 */
public enum RideEndpoints implements Endpoints {

    BASE_URI("/api/rides"),

    SAVE_RIDE("/api/rides"),

    GET_RIDE("/api/rides/{rideId}"),

    CHECK_COST("/api/rides/cost"),

    UPDATE_RIDE("/api/rides/{rideId}"),

    DELETE_RIDE("/api/rides/{rideId}"),

    ACTIVATE_RIDE("/api/rides/{rideId}/activate"),

    ASSIGN_RIDE("/api/rides/{rideId}/assign"),

    CANCEL_RIDE("/api/rides/{rideId}/cancel"),

    COMPLETE_RIDE("/api/rides/{rideId}/done"),

    RATE_RIDE("/api/rides/{rideId}/rate");

    private final String uri;

    RideEndpoints(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }

}
