package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.cet325.bg72db.ExchangeRates.ExchangeRatesUtils;
import com.cet325.bg72db.ExchangeRates.JSONExchangeRatesFormatter;
import com.cet325.bg72db.ExchangeRates.Models.ExchangeRates;

import org.json.JSONException;

public class TicketInformationActivity extends AppCompatActivity {

    ActionBar actionBar = null;
    TextView pricesTextView = null;
    FloatingActionButton currencyExchangeBtn = null;

    SharedPreferences sharedPreferences = null;
    double TICKET_PRICE_EURO = 10.00;
    double gbpExchangeRate = 0.88;
    String selectedCurrency;

    ExchangeRatesUtils exchangeRatesUtils = null;

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

        exchangeRatesUtils = new ExchangeRatesUtils();

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        selectedCurrency = sharedPreferences.getString(
                getResources().getString(R.string.key_default_currency),
                getResources().getString(R.string.default_currency)
        );

        currencyExchangeBtn = (FloatingActionButton) findViewById(R.id.currency_action_button);
        currencyExchangeBtn.setOnClickListener(currencyExchangeEventListener);

        pricesTextView = (TextView) findViewById(R.id.prices_text_view);
        updatePricesTextView(selectedCurrency);

        JSONExchangeRateTask task = new JSONExchangeRateTask();
        task.execute("EUR");
    }

    @Override
    public void onPause(){
        super.onPause();
        updateSharedPreferences();
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
            case R.id.paintings_link:
                Intent paintings_activity_intent = new Intent(getApplicationContext(), PaintingMasterActivity.class);
                startActivity(paintings_activity_intent);
            case R.id.find_us:
                Intent findUsActivityIntent = new Intent(getApplicationContext(), FindUsActivity.class);
                startActivity(findUsActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buildCurrencyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.currency_dialog_title)
                .setItems(R.array.currencies, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selected) {
                        String[] currencies = getResources().getStringArray(R.array.currencies);
                        String[] currencyCodes = getResources().getStringArray(R.array.currency_codes);
                        for (int i = 0; i < currencies.length; i++) {
                            if (selected == i) {
                                selectedCurrency = currencyCodes[i];
                                updatePricesTextView(selectedCurrency);
                                updateSharedPreferences();
                            }
                        }
                    }
                })
                .create()
                .show();
    }

    private void updatePricesTextView(String currency) {
        double price = 0.0;
        String currencySymbol = "";

        switch (currency) {
            case "EUR":
                price = TICKET_PRICE_EURO;
                currencySymbol = "€";
                break;
            case "GBP":
                price = 10 * gbpExchangeRate;
                currencySymbol = "£";
                break;
        }

        pricesTextView.setText(getString(
                R.string.currency_final_string,
                currencySymbol,
                exchangeRatesUtils.formatCurrencyPrice(currency, price),
                exchangeRatesUtils.formatCurrencyPrice(currency, price * .7)
        ));
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_default_currency), selectedCurrency);
        editor.apply();
    }

    private class JSONExchangeRateTask extends AsyncTask<String, Void, ExchangeRates> {

        @Override
        protected ExchangeRates doInBackground(String... params) {
            ExchangeRates rates = null;
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


