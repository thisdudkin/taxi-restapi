package by.dudkin.driver.util;

import by.dudkin.common.util.Endpoints;

/**
 * @author Alexander Dudkin
 */
public enum CarEndpoints implements Endpoints {

    BASE_URI("/api/cars"),

    SAVE_CAR("/api/cars"),

    GET_CAR("/api/cars/{carId}"),

    UPDATE_CAR("/api/cars/{carId}"),

    DELETE_CAR("/api/cars/{carId}");

    private final String uri;

    CarEndpoints(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
