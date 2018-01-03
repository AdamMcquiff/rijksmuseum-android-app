package com.cet325.bg72db;

import com.cet325.bg72db.ExchangeRates.ExchangeRatesHttpClient;
import com.cet325.bg72db.ExchangeRates.ExchangeRatesUtils;
import com.cet325.bg72db.ExchangeRates.JSONExchangeRatesFormatter;
import com.cet325.bg72db.ExchangeRates.Models.ExchangeRates;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class ExchangeRatesUnitTests {

    @Test
    public void testJSONExchangeRatesFormatterCorrectlyFormats() {
        // Get Data from Fixer.io API
        String data = new ExchangeRatesHttpClient().getLatestRates("EUR");

        // Get Data from JSON object to compare against data retrieved from JSONFormatter class
        JSONObject jsonObj;
        double GBP = 0.0;
        String base = "EUR";
        try {
            jsonObj = new JSONObject(data);
            GBP = jsonObj.getJSONObject("rates").getDouble("GBP");
            base = jsonObj.getString("base");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get data from JSONFormatter class and store in 'Rates' model object instance
        ExchangeRates rates = new ExchangeRates();
        try {
            rates = JSONExchangeRatesFormatter.getExchangeRates(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Confirm data is the same, thus confirming the JSONFormatter is working correctly
        assertEquals("Formatting of GBP failed.", rates.getGBP(), GBP, 0.001);
        assertEquals("Formatting of Base currency failed.", rates.getBase(), base);
    }

    @Test
    public void testExchangeRatesUtilsBuildCurrencyStringCorrectlyFormatsEUR() {
        // Get initial price
        double price = 10.30;

        // Setup Decimal formatter to compare against ExchangeRateUtil formatter; setup to
        // format currency in EUR
        DecimalFormat df = new DecimalFormat(".##");
        df.setMinimumFractionDigits(2);
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(otherSymbols);

        // Initialise ExchangeRateUtils class
        ExchangeRatesUtils exchangeRatesUtils = new ExchangeRatesUtils();

        // Confirm our DF matches exchangeRateUtil formatter
        assertEquals(
                "Formatting of EUR price failed.",
                exchangeRatesUtils.formatCurrencyPrice("EUR", price),
                df.format(price)
        );
    }

}