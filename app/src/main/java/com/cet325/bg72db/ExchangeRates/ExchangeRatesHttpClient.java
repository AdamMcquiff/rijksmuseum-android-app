package com.cet325.bg72db.ExchangeRates;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ExchangeRatesHttpClient {

    //https://api.fixer.io/latest?base=EUR
    private static String BASE_URL = "https://api.fixer.io/latest?base=";

    public String getLatestRates(String baseCurrency) {
        HttpURLConnection con = null;
        InputStream is = null;
        String urlString = "";

        try {
            urlString = BASE_URL + URLEncoder.encode(baseCurrency, "UTF-8");
            Log.d("urlString", urlString);
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
                Log.d("JSON-line", line);
                buffer.append(line + "\r\n");
            }
            is.close();
            con.disconnect();
            Log.d("JSON", buffer.toString());
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { is.close(); } catch (Exception e) { e.printStackTrace(); }
            try { con.disconnect(); } catch (Exception e) { e.printStackTrace(); }
        }

        return null;
    }
}
