package com.cet325.bg72db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    ScrollView main_activity = null;
    Button call_to_action_btn = null;

    private View.OnClickListener call_to_action_event_listener = new View.OnClickListener() {
        public void onClick(View view) {
            // TODO; add intent / event logic
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_activity = findViewById(R.id.main_activity);
        registerForContextMenu(main_activity);

        call_to_action_btn = findViewById(R.id.discover_art_btn);
        call_to_action_btn.setOnClickListener(call_to_action_event_listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                // String name = mEditTextName.getText().toString();
                Intent ticket_info_activity_intent = new Intent(getApplicationContext(), TicketInformationActivity.class);
                // myIntent.putExtra("name", name);
                startActivity(ticket_info_activity_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
