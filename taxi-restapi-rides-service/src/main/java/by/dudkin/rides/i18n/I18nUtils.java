package by.dudkin.rides.i18n;

import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Dudkin
 */
@Component
public class I18nUtils {

    private final MessageSource messageSource;

    @Resource(name = "localeHolder")
    LocaleHolder localeHolder;

    public I18nUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, localeHolder.getCurrentLocale());
    }

}
