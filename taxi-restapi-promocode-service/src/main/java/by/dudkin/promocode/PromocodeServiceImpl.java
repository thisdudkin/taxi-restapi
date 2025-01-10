package by.dudkin.promocode;

import by.dudkin.common.util.ErrorMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
class PromocodeServiceImpl implements PromocodeService {

    final PromocodeRepository promocodeRepository;

    PromocodeServiceImpl(PromocodeRepository promocodeRepository) {
        this.promocodeRepository = promocodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Promocode validate(String code) {
        return promocodeRepository.getByCode(code)
            .orElseThrow(() -> new PromocodeNotFoundException(ErrorMessages.PROMOCODE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Promocode> getActivePromocodes(int page, int size) {
        return promocodeRepository.getActivePromocodes(page, size);
    }

    @Override
    public void deleteExpiredPromocodes() {
        promocodeRepository.deleteRecentPromocodes();
    }

}
