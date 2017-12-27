package com.cet325.bg72db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cet325.bg72db.SQLite.Models.Painting;
import com.cet325.bg72db.SQLite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class PaintingsMasterActivity extends AppCompatActivity {

    ActionBar action_bar = null;
    SQLiteHelper SqLiteHelper = null;
    List<Painting> allPaintings = null;
    ListView paintingsListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintings);

        action_bar = getSupportActionBar();
        if (action_bar != null) action_bar.setDisplayHomeAsUpEnabled(true);

        SqLiteHelper = new SQLiteHelper(getApplicationContext());
        allPaintings = SqLiteHelper.getAllPaintings();

        paintingsListView = findViewById(R.id.paintings_list_view);
        final ArrayList<Painting> paintingsArrayList = new ArrayList<>(allPaintings);
        String[] listItems = new String[paintingsArrayList.size()];
        for (int i = 0; i < paintingsArrayList.size(); i++){
            Painting painting = paintingsArrayList.get(i);
            listItems[i] = painting.getTitle();
        }
        PaintingAdapter adapter = new PaintingAdapter(this, paintingsArrayList);
        Log.d("allPaintings", allPaintings.toString());
        paintingsListView.setAdapter(adapter);
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
