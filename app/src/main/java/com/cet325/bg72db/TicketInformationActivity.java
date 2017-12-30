package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cet325.bg72db.ExchangeRates.ExchangeRatesHttpClient;
import com.cet325.bg72db.ExchangeRates.JSONExchangeRatesFormatter;
import com.cet325.bg72db.ExchangeRates.Models.ExchangeRates;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TicketInformationActivity extends AppCompatActivity {

    ActionBar actionBar = null;
    TextView pricesTextView = null;
    FloatingActionButton currencyExchangeBtn = null;

    SharedPreferences sharedPreferences = null;
    static double TICKET_PRICE_EURO = 10.00;
    double gbpExchangeRate = 0.88;
    String selectedCurrency;

    private View.OnClickListener currencyExchangeEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            buildCurrencyDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_information);

        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        selectedCurrency = sharedPreferences.getString(
                getResources().getString(R.string.key_default_currency),
                getResources().getString(R.string.default_currency)
        );

        currencyExchangeBtn = findViewById(R.id.currency_action_button);
        currencyExchangeBtn.setOnClickListener(currencyExchangeEventListener);

        pricesTextView = findViewById(R.id.prices_text_view);
        updatePricesTextView(selectedCurrency);

        JSONExchangeRateTask task = new JSONExchangeRateTask();
        task.execute(selectedCurrency);
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_default_currency), selectedCurrency);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buildCurrencyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.currency_dialog_title);
        builder.setItems(R.array.currencies, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selected) {
                String[] currencies = getResources().getStringArray(R.array.currencies);
                String[] currencyCodes = getResources().getStringArray(R.array.currency_codes);
                for (int i = 0; i < currencies.length; i++) {
                    if (selected == i) {
                        selectedCurrency = currencyCodes[i];
                        updatePricesTextView(selectedCurrency);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.key_default_currency), selectedCurrency);
                        editor.apply();
                    }
                }
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void updatePricesTextView(String currency) {
        DecimalFormat df = new DecimalFormat(".##");
        df.setMinimumFractionDigits(2);
        double ticketPrice = 0.0;
        String currencySymbol = "";
        switch (currency) {
            case "EUR":
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
                otherSymbols.setDecimalSeparator(',');
                df.setDecimalFormatSymbols(otherSymbols);
                ticketPrice = TICKET_PRICE_EURO;
                currencySymbol = "€";
                break;
            case "GDP":
                ticketPrice = 10 * gbpExchangeRate;
                currencySymbol = "£";
                break;
        }
        pricesTextView.setText(getString(R.string.currency_final_string, currencySymbol, df.format(ticketPrice), df.format(ticketPrice * .7)));
    }

    private class JSONExchangeRateTask extends AsyncTask<String, Void, ExchangeRates> {
        @Override
        protected ExchangeRates doInBackground(String... params) {
            ExchangeRates rates = new ExchangeRates();
            String data = new ExchangeRatesHttpClient().getLatestRates(params[0]);
            if (data == null) return null;
            try {
                rates = JSONExchangeRatesFormatter.getExchangeRates(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rates;
        }

        @Override
        protected void onPostExecute(ExchangeRates rates) {
            super.onPostExecute(rates);
            if (rates != null) gbpExchangeRate = rates.getGBP();
        }
    }

}


