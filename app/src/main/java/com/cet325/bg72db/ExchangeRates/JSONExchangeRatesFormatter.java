package com.cet325.bg72db.ExchangeRates;

import com.cet325.bg72db.ExchangeRates.Models.ExchangeRates;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to format the Exchange Rate data captured by the HTTP client class. Creates an instance
 * of the ExchangeRates model and stores data retrieved from the API into the instance, which
 * is then used as needed.
 */
public class JSONExchangeRatesFormatter {

    /**
     * @param data the data returned from the API.
     * @return an instance of ExchangeRates populated with data from the exchange rate API.
     * @throws JSONException ...
     */
    public static ExchangeRates getExchangeRates(String data) throws JSONException {
        ExchangeRates rates = new ExchangeRates();
        JSONObject jsonObj = new JSONObject(data);
        rates.setBase(jsonObj.getString("base"));
        rates.setGBP(jsonObj.getJSONObject("rates").getDouble("GBP"));
        return rates;
    }

}
