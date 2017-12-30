package com.cet325.bg72db.ExchangeRates;

import com.cet325.bg72db.ExchangeRates.Models.ExchangeRates;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONExchangeRatesFormatter {

    public static ExchangeRates getExchangeRates(String data) throws JSONException {
        ExchangeRates rates = new ExchangeRates();
        JSONObject jsonObj = new JSONObject(data);
        rates.setBase(jsonObj.getString("base"));
        rates.setGBP(jsonObj.getJSONObject("rates").getDouble("GBP"));
        return rates;
    }

}
