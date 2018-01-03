package com.cet325.bg72db.ExchangeRates;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class with several utilities usable for ticket exchange rates to help keep activity logic
 * to a minimum and improve code re-usability.
 */
public class ExchangeRatesUtils {


    /**
     * This method takes a currency code (e.g. "EUR") and a price, then formats the price into
     * the specific currency format. E.g. 10.00 for EUR would be formatted into "10,00"
     *
     * @param currency the currency which the price will be formatted to.
     * @param price the price (double) that will be formatted into a string.
     * @return the price formatted into a currency-specific string.
     */
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
