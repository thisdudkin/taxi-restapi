package by.dudkin.promocode;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class PromocodeNotFoundException extends RuntimeException {
    public PromocodeNotFoundException(String message) {
        super(message);
    }
}
