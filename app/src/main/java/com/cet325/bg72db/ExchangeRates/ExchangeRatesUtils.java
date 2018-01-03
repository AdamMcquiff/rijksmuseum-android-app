package com.cet325.bg72db.ExchangeRates;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class with several utilities usable for ticket exchange rates to help keep activity logic
 * to a minimum and improve code re-usability.
 */
public class ExchangeRatesUtils {

    public String formatCurrencyPrice(String currency, double price) {
        DecimalFormat df = new DecimalFormat(".##");
        df.setMinimumFractionDigits(2);

        switch (currency) {
            case "EUR":
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
                otherSymbols.setDecimalSeparator(',');
                df.setDecimalFormatSymbols(otherSymbols);
                break;
        }

        return df.format(price);
    }

}
