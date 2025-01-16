package by.dudkin.promocode;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * @author Alexander Dudkin
 */
interface PromocodeRepository {
    void savePromocodes(Collection<Promocode> promocodes);
    Optional<Promocode> getByCode(String code);
    Set<Promocode> getActivePromocodes(int page, int size);
    void deleteRecentPromocodes();
}
