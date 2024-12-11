package by.dudkin.driver.repository;

import by.dudkin.driver.domain.DriverLocationDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Set;

/**
 * @author Alexander Dudkin
 */
public interface DriverLocationRepository extends ElasticsearchRepository<DriverLocationDocument, Long> {

    @Query("""
        {
          "geo_distance": {
            "distance": "3km",
            "location": {
              "lat": "?0",
              "lon": "?1"
            }
          }
        }
        """)
    Set<DriverLocationDocument> findDriversInRange(double lat, double lng);

}
