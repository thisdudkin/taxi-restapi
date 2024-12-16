package by.dudkin.driver.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.UUID;

/**
  * @author Alexander Dudkin
  */
@Document(indexName = "drivers")
public record DriverLocationDocument(
    @Id UUID driverId,

    @Field
    GeoPoint location
) {}
