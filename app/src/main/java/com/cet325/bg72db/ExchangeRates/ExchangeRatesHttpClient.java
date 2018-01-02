package com.cet325.bg72db.ExchangeRates;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Class to serve as an intractable HTTP client between the Android application and the Exchange Rate
 * API, Fixer. The client allows the app to get the latest exchange rates.
 */
public class ExchangeRatesHttpClient {

    public String getLatestRates(String baseCurrency) {
        HttpURLConnection con = null;
        InputStream is = null;
        String urlString = "";

        try {
            String BASE_URL = "https://api.fixer.io/latest?base=";
            urlString = BASE_URL + URLEncoder.encode(baseCurrency, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            con = (HttpURLConnection) (new URL(urlString)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.connect();

            int response = con.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                Log.d("HttpURLConnection","Unable to connect");
                return null;
            }
            StringBuilder buffer = new StringBuilder();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\r\n");
            }
            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) try { is.close(); } catch (Exception e) { e.printStackTrace(); }
            if (con != null) try { con.disconnect(); } catch (Exception e) { e.printStackTrace(); }
        }
        return null;
    }
}
