package by.dudkin.promocode;

import java.util.Set;

/**
 * @author Alexander Dudkin
 */
interface PromocodeService {
    Promocode validate(String code);
    Set<Promocode> getActivePromocodes(int page, int size);
    void deleteExpiredPromocodes();
}
