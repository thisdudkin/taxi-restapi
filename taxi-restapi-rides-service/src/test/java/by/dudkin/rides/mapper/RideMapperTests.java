package by.dudkin.rides.mapper;

import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.rides.domain.Location;
import by.dudkin.rides.domain.Ride;
import by.dudkin.rides.rest.dto.request.RideRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Alexander Dudkin
 */
class RideMapperTests {

    private final RideMapper rideMapper = Mappers.getMapper(RideMapper.class);

    private static final Long DEFAULT_PASSENGER_ID = 10L;
    private static final Long DEFAULT_DRIVER_ID = 20L;
    private static final Long DEFAULT_CAR_ID = 30L;
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.TEN;
    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.CASH;
    private static final String COUNTRY_BELARUS = "Belarus";
    private static final String CITY_MINSK = "Minsk";
    private static final String STREET_ONE = "One";
    private static final String STREET_TWO = "Two";
    private static final Location DEFAULT_LOCATION_FROM = createLocation(STREET_ONE);
    private static final Location DEFAULT_LOCATION_TO = createLocation(STREET_TWO);

    private static Location createLocation(String street) {
        return Location.builder()
            .country(COUNTRY_BELARUS)
            .city(CITY_MINSK)
            .street(street)
            .build();
    }

    private Ride createRide() {
        return Ride.builder()
            .id(1L)
            .passengerId(DEFAULT_PASSENGER_ID)
            .driverId(DEFAULT_DRIVER_ID)
            .carId(DEFAULT_CAR_ID)
            .from(DEFAULT_LOCATION_FROM)
            .to(DEFAULT_LOCATION_TO)
            .price(DEFAULT_PRICE)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusMinutes(5))
            .build();
    }

    private RideRequest createRideRequest() {
        return new RideRequest(
            DEFAULT_PASSENGER_ID,
            DEFAULT_LOCATION_FROM,
            DEFAULT_LOCATION_TO,
            DEFAULT_PAYMENT_METHOD);
    }

    @Test
    void shouldMapRideToResponse() {
        var ride = createRide();
        var response = rideMapper.toResponse(ride);

        assertNotNull(response);
        assertEquals(ride.getId(), response.id());
        assertEquals(ride.getFrom(), response.from());
        assertEquals(ride.getTo(), response.to());
    }

    @Test
    void shouldMapRequestToRide() {
        var request = createRideRequest();
        var ride = rideMapper.toRide(request);

        assertNotNull(ride);
        assertEquals(request.from().getCity(), ride.getFrom().getCity());
    }

    @Test
    void shouldUpdateRideFromRideRequest() {
        var ride = new Ride();
        var request = createRideRequest();

        rideMapper.updateRide(request, ride);

        assertEquals(request.passengerId(), ride.getPassengerId());
        assertEquals(request.from().getCity(), ride.getFrom().getCity());
        assertEquals(request.to().getCity(), ride.getTo().getCity());
        assertEquals(request.paymentMethod(), ride.getPaymentMethod());
    }

}
