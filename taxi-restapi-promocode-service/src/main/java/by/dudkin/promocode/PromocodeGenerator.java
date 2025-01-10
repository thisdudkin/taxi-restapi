package by.dudkin.promocode;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static by.dudkin.promocode.PromocodeUtils.*;

/**
 * @author Alexander Dudkin
 */
@Component
class PromocodeGenerator implements Generator {

    @Override
    public List<Promocode> generate(int count) {
        List<Promocode> promocodes = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            promocodes.add(generatePromocode());
        }

        return Collections.unmodifiableList(promocodes);
    }

    private Promocode generatePromocode() {
        return new Promocode(
            null,
            generateCode(),
            generateDiscount(),
            null
        );
    }

    private String generateCode() {
        return RandomStringUtils.random(CODE_LENGTH, true, true).toUpperCase();
    }

    private int generateDiscount() {
        return ThreadLocalRandom.current().nextInt(MAX_DISCOUNT - MIN_DISCOUNT + 1) + MIN_DISCOUNT;
    }

}
