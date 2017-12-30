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

    ScrollView mainActivity = null;
    Button callToActionBtn = null;

    private View.OnClickListener callToActionEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent paintings_activity_intent = new Intent(getApplicationContext(), PaintingMasterActivity.class);
            startActivity(paintings_activity_intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = findViewById(R.id.main_activity);
        registerForContextMenu(mainActivity);

        callToActionBtn = findViewById(R.id.discover_art_btn);
        callToActionBtn.setOnClickListener(callToActionEventListener);
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
                Intent ticketInfoActivityIntent = new Intent(getApplicationContext(), TicketInformationActivity.class);
                startActivity(ticketInfoActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupDB() {
        SQLiteHelper db = new SQLiteHelper(this);
        if (db.getAllPaintings().size() == 0) {
            db.addPainting(new Painting("Caesar Boëtius van Everdingen", "Willem Jacobsz Baert (1636-84), Burgomaster of Alkmaar and Amsterdam", "A12", "Portret van Willem Jacobsz Baert, burgemeester van Alkmaar en Amsterdam. Ten halven lijve, staande met een handschoen in de linkerhand. Pendant van SK-A-1340.", "", 1671, 0));
            db.addPainting(new Painting("Bartolommeo Vivarini", "Saint Cosmas (or Damian)", "C32", "Halffiguur van de heilige Cosmas (of misschien Damianus), in de hand een vierkante zalfdoos. Pendant van SK-A-4012.", "", 1460, 0));
            db.addPainting(new Painting("Maarten van Heemskerck", "Portraits of a Couple", "D2", "Portret van een vrouw, vroeger geïdentificeerd als Anna Codde, de echtgenote van Pieter Gerritsz Bicker. Zittend, ten halven lijve, spinnend aan een spinnewiel. Pendant van SK-A-3518.", "", 1529, 0));
            db.addPainting(new Painting("George Hendrik Breitner", "Ginger Pot wit Anemones", "D6", "Stilleven van een gemberpot met anemonen.", "", 1923, 0));
            db.addPainting(new Painting("Abraham Teerlink", "Staande vrouw in Italiaanse klederdracht", "B23", "--", "", 1857, 0));
            db.addPainting(new Painting("Johan Braakensiek", "Zittend meisje in klederdracht", "B23", "--", "", 1940, 0));
            db.addPainting(new Painting("Johannes Frederik Engelbert ten Klooster", "Dalang, Johannes Frederik Engelbert ten Klooster", "A12", "--", "", 1928, 0));
            db.addPainting(new Painting("Yashima Gakutei", "Dansende geisha's, Yashima Gakutei", "A5", "Twee geisha's dansen op een podium met lantarens boven hun hoofd, met in de achtergrond kersenbloesems en pijnbomen. Dit is het rechterblad van het vijfluik. Met twee gedichten.", "", 1824, 0));
            db.addPainting(new Painting("Tokuriki Tomikichirô", "Vergezicht op de berg Fuji vanaf het station Kiyosato in Nagano", "C4", "Een bergketen waarachter de top van de besneeuwde berg Fuji; op de voorgrond een vlakte met voornamelijk kale bomen.", "", 1940, 0));
            db.addPainting(new Painting("Thomas James Dixon", "Tijger in een dierentuin", "D65", "--", "", 1880, 0));
        }
    }
}
