package by.dudkin.i18n;

import java.util.Locale;

/**
 * @author Alexander Dudkin
 */
public class LocaleHolder {
    private Locale currentLocale;

    public LocaleHolder() { }

    public LocaleHolder(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    @Override
    public String toString() {
        return currentLocale.toString();
    }

}
