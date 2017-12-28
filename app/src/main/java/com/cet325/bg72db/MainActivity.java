package com.cet325.bg72db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.cet325.bg72db.SQLite.Models.Painting;
import com.cet325.bg72db.SQLite.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    ScrollView main_activity = null;
    Button call_to_action_btn = null;

    private View.OnClickListener call_to_action_event_listener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent paintings_activity_intent = new Intent(getApplicationContext(), PaintingMasterActivity.class);
            startActivity(paintings_activity_intent);
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
    public void onStart() {
        super.onStart();
        setupDB();
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

    private void setupDB() {
        SQLiteHelper db = new SQLiteHelper(this);
        // TODO: empty DB and figure out a way to only add if the DB doesn't exist
        db.addPainting(new Painting("Caesar Boëtius van Everdingen", "Willem Jacobsz Baert (1636-84), Burgomaster of Alkmaar and Amsterdam", "", "Portret van Willem Jacobsz Baert, burgemeester van Alkmaar en Amsterdam. Ten halven lijve, staande met een handschoen in de linkerhand. Pendant van SK-A-1340.", "", 1671, 3));
        db.addPainting(new Painting("Bartolommeo Vivarini", "Saint Cosmas (or Damian)", "", "Halffiguur van de heilige Cosmas (of misschien Damianus), in de hand een vierkante zalfdoos. Pendant van SK-A-4012.", "", 1460, 3));
        db.addPainting(new Painting("Maarten van Heemskerck", "Portraits of a Couple", "", "Portret van een vrouw, vroeger geïdentificeerd als Anna Codde, de echtgenote van Pieter Gerritsz Bicker. Zittend, ten halven lijve, spinnend aan een spinnewiel. Pendant van SK-A-3518.", "", 1529, 4));
        db.addPainting(new Painting("George Hendrik Breitner", "Ginger Pot wit Anemones", "", "Stilleven van een gemberpot met anemonen.", "", 1923, 3));
        db.addPainting(new Painting("Abraham Teerlink", "Staande vrouw in Italiaanse klederdracht", "", "--", "", 1857, 3));
        db.addPainting(new Painting("Johan Braakensiek", "Zittend meisje in klederdracht", "", "--", "", 1940, 3));
        db.addPainting(new Painting("Johannes Frederik Engelbert ten Klooster", "Dalang, Johannes Frederik Engelbert ten Klooster", "", "--", "", 1928, 3));
        db.addPainting(new Painting("Yashima Gakutei", "Dansende geisha's, Yashima Gakutei", "", "Twee geisha's dansen op een podium met lantarens boven hun hoofd, met in de achtergrond kersenbloesems en pijnbomen. Dit is het rechterblad van het vijfluik. Met twee gedichten.", "", 1824, 3));
        db.addPainting(new Painting("Tokuriki Tomikichirô", "Vergezicht op de berg Fuji vanaf het station Kiyosato in Nagano", "", "Een bergketen waarachter de top van de besneeuwde berg Fuji; op de voorgrond een vlakte met voornamelijk kale bomen.", "", 1940, 5 ));
        db.addPainting(new Painting("Thomas James Dixon", "Tijger in een dierentuin", "", "--", "", 1880, 4));
        db.getPainting(13);
    }
}
