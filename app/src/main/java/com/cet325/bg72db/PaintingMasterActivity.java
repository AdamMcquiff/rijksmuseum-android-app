package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cet325.bg72db.SQLite.Models.Painting;
import com.cet325.bg72db.SQLite.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PaintingMasterActivity extends AppCompatActivity {

    ActionBar actionBar = null;
    FloatingActionButton sortBtn = null;
    FloatingActionButton filterBtn = null;
    FloatingActionButton addBtn = null;

    SQLiteHelper SqLiteHelper = null;
    List<Painting> allPaintings = null;
    ListView paintingsListView = null;
    ArrayList<Painting> paintingsArrayList = null;

    private View.OnClickListener sortRowsEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            buildRowSortDialog();
        }
    };

    private View.OnClickListener filterRowsEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            buildRowFilterDialog();
        }
    };

    private AdapterView.OnItemClickListener rowEventListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Painting painting = paintingsArrayList.get(position);
            Intent detailIntent = new Intent(getApplicationContext(), PaintingDetailActivity.class);
            detailIntent.putExtra("title", painting.getTitle());
            detailIntent.putExtra("artist", painting.getArtist());
            startActivity(detailIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_master);

        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        sortBtn = findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(sortRowsEventListener);
        filterBtn = findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(filterRowsEventListener);
        addBtn = findViewById(R.id.add_btn);

        SqLiteHelper = new SQLiteHelper(getApplicationContext());
        allPaintings = SqLiteHelper.getAllPaintings();

        paintingsListView = findViewById(R.id.paintings_list_view);
        paintingsListView.setOnItemClickListener(rowEventListener);
        paintingsArrayList = new ArrayList<>(allPaintings);
        sortRows(1);
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

    private void buildRowSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.currency_dialog_title);
        builder.setItems(R.array.sortable_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selected) {
                sortRows(selected);
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void buildRowFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.currency_dialog_title);
        builder.setItems(R.array.filter_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selected) {
                filterRows(selected);
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void sortRows(int selected) {
        switch (selected) {
            case 0:
                Collections.sort(paintingsArrayList, new Comparator<Painting>(){
                    public int compare(Painting a, Painting b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case 1:
                Collections.sort(paintingsArrayList, new Comparator<Painting>(){
                    public int compare(Painting a, Painting b) {
                        int artistCompare = a.getArtist().compareTo(b.getArtist());
                        return artistCompare != 0
                            ? artistCompare
                            : a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case 2:
                Collections.sort(paintingsArrayList, new Comparator<Painting>(){
                    public int compare(Painting a, Painting b) {
                        return a.getRank() - b.getRank();
                    }
                });
                break;
            default:
                break;
        }
        setupRowAdapter();
    }

    private void filterRows(int selected) {
        List<Painting> paintings = new ArrayList<>();
        paintingsArrayList = new ArrayList<>(allPaintings);
        switch (selected) {
            case 1:
                for (int i = 0; i < paintingsArrayList.size(); i++) {
                    Painting painting = paintingsArrayList.get(i);
                    if (painting.getRank() != 0) paintings.add(painting);
                }
                paintingsArrayList = new ArrayList<>(paintings);
                break;
            case 2:
                for (int i = 0; i < paintingsArrayList.size(); i++) {
                    Painting painting = paintingsArrayList.get(i);
                    if (painting.getRank() == 0) paintings.add(painting);
                }
                paintingsArrayList = new ArrayList<>(paintings);
                break;
        }
        setupRowAdapter();
    }

    private void setupRowAdapter() {
        PaintingAdapter adapter = new PaintingAdapter(this, paintingsArrayList);
        paintingsListView.setAdapter(adapter);
    }

}
