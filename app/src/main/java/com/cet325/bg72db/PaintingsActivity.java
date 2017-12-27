package com.cet325.bg72db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cet325.bg72db.SQLite.Models.Painting;
import com.cet325.bg72db.SQLite.SQLiteHelper;

import java.util.List;

public class PaintingsActivity extends AppCompatActivity {

    ActionBar action_bar = null;
    SQLiteHelper SqLiteHelper = null;
    List<Painting> allPaintings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintings);

        action_bar = getSupportActionBar();
        if (action_bar != null) action_bar.setDisplayHomeAsUpEnabled(true);

        SqLiteHelper = new SQLiteHelper(getApplicationContext());
        allPaintings = SqLiteHelper.getAllPaintings();
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
            case R.id.action_info:
                Intent ticket_info_activity_intent = new Intent(getApplicationContext(), TicketInformationActivity.class);
                startActivity(ticket_info_activity_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
