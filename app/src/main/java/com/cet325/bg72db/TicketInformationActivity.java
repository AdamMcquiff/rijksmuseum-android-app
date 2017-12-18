package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TicketInformationActivity extends AppCompatActivity {

    ActionBar action_bar = null;
    TextView prices_textview = null;
    FloatingActionButton currency_exchange_btn = null;

    static double TICKET_PRICE_EURO = 10.00;
    // TODO: get user currency OR default
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

        prices_textview = findViewById(R.id.prices_textview);
        updatePricesTextView(selected_currency);

        currency_exchange_btn = findViewById(R.id.currency_action_button);
        currency_exchange_btn.setOnClickListener(currency_exchange_event_listener);
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
        switch (currency) {
            case "EUR":
                string += "€10,00 \nStudent: €7,00";
                break;
            case "GDP":
                string += "£10.00 \nStudent: £7.00";
                break;
        }
        string += "\nChildren (under 18): FREE";
        prices_textview.setText(string);
    }
}
