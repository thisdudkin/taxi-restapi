package by.dudkin.promocode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Alexander Dudkin
 */
@RestController
class PromocodeRestController implements PromocodeApi {

    final PromocodeService promocodeService;

    PromocodeRestController(PromocodeService promocodeService) {
        this.promocodeService = promocodeService;
    }

    @Override
    public ResponseEntity<Promocode> validatePromocode(String code) {
        return ResponseEntity.status(200).body(this.promocodeService.validate(code));
    }

    @Override
    public ResponseEntity<Set<Promocode>> getActivePromocodes(int page, int size) {
        return ResponseEntity.status(200).body(this.promocodeService.getActivePromocodes(page, size));
    }

    @Override
    public ResponseEntity<Void> deleteExpiredPromocodes() {
        this.promocodeService.deleteExpiredPromocodes();
        return ResponseEntity.noContent().build();
    }

}
