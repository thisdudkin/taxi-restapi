package by.dudkin.promocode;

import java.util.List;

/**
 * @author Alexander Dudkin
 */
interface Generator {
    List<Promocode> generate(int count);
}
