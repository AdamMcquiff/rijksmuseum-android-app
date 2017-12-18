package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class TicketInformationActivity extends AppCompatActivity {

    ActionBar action_bar = null;
    TextView prices_text_view = null;
    FloatingActionButton currency_exchange_btn = null;

    static double TICKET_PRICE_EURO = 10.00;
    double gbp_exchange_rate = 0.88;
    String selected_currency = "EUR";

    private View.OnClickListener currency_exchange_event_listener = new View.OnClickListener() {
        public void onClick(View view) {
            buildCurrencyDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_information);

        action_bar = getSupportActionBar();
        if (action_bar != null) action_bar.setDisplayHomeAsUpEnabled(true);

        prices_text_view = findViewById(R.id.prices_textview);
        updatePricesTextView(selected_currency);

        currency_exchange_btn = findViewById(R.id.currency_action_button);
        currency_exchange_btn.setOnClickListener(currency_exchange_event_listener);

        JSONExchangeRateTask task = new JSONExchangeRateTask();
        task.execute("EUR");
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
                String[] currency_codes = getResources().getStringArray(R.array.currency_codes);
                for (int i = 0; i < currencies.length; i++) {
                    if (selected == i) {
                        // TODO: make this persistent (presumably in the database)
                        selected_currency = currency_codes[i];
                        updatePricesTextView(selected_currency);
                        System.out.println("Current index is: " + i);
                    }
                }
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void updatePricesTextView(String currency) {
        String string = "Adult (18+): ";
        DecimalFormat df = new DecimalFormat(".##"); // TODO: make this word for Euro in format X,XX
        df.setMinimumFractionDigits(2);
        double ticketPrice;
        switch (currency) {
            case "EUR":
                ticketPrice = TICKET_PRICE_EURO;
                string += "€" + df.format(ticketPrice) + "\nStudent: €" + df.format(ticketPrice * .7);
                break;
            case "GDP":
                ticketPrice = 10 * gbp_exchange_rate;
                string += "£" + df.format(ticketPrice) + "\nStudent: £" + df.format(ticketPrice * .7);
                break;
        }
        string += "\nChildren (under 18): FREE";
        prices_text_view.setText(string);
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
            gbp_exchange_rate = rates.getGBP();
        }

    }
}


