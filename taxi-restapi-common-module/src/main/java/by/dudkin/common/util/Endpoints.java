package by.dudkin.common.util;

import java.util.stream.Stream;

/**
 * @author Alexander Dudkin
 */
public interface Endpoints {

    String getURI();

    default String[] getAllURIs() {
        return Stream.of(this.getClass().getEnumConstants())
            .map(Endpoints::getURI)
            .toArray(String[]::new);
    }

}
