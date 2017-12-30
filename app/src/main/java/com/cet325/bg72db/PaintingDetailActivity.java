package com.cet325.bg72db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PaintingDetailActivity extends AppCompatActivity {

    ActionBar actionBar = null;
    TextView paintingTitle = null;
    TextView paintingArtist = null;
    TextView paintingYear = null;
    TextView paintingDescription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_detail);

        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        paintingTitle = findViewById(R.id.painting_title);
        paintingArtist = findViewById(R.id.painting_artist);
        paintingYear = findViewById(R.id.painting_year);
        paintingDescription = findViewById(R.id.painting_description);
        paintingTitle.setText(this.getIntent().getExtras().getString("title"));
        paintingArtist.setText(this.getIntent().getExtras().getString("artist"));
        paintingYear.setText(this.getIntent().getExtras().getString("year"));
        paintingDescription.setText(this.getIntent().getExtras().getString("description"));

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
                Intent ticketInfoActivityIntent = new Intent(getApplicationContext(), TicketInformationActivity.class);
                startActivity(ticketInfoActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
