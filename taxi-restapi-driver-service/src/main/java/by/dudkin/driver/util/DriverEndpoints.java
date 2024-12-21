package by.dudkin.driver.util;

import by.dudkin.common.util.Endpoints;

import java.util.stream.Stream;

/**
 * @author Alexander Dudkin
 */
public enum DriverEndpoints implements Endpoints {

    BASE_URI("/api/drivers"),

    GET_ALL_DRIVERS("/api/drivers"),

    SAVE_DRIVER("/api/drivers"),

    GET_DRIVER_BY_ID("/api/drivers/{driverId}"),

    DELETE_DRIVER("/api/drivers/{driverId}"),

    UPDATE_DRIVER("/api/drivers/{driverId}"),

    UPDATE_DRIVER_LOCATION("/api/drivers/{driverId}/location"),

    UPDATE_DRIVER_BALANCE("/api/drivers/balance"),

    MARK_DRIVER_AVAILABLE("/api/drivers/{driverId}/status/available"),

    MARK_DRIVER_BUSY("/api/drivers/{driverId}/status/busy"),

    MARK_DRIVER_OFFLINE("/api/drivers/{driverId}/status/offline");

    private final String uri;

    DriverEndpoints(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }

}
