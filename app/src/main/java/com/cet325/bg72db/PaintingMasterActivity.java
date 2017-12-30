package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    private View.OnClickListener addPaintingDialogEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            buildAddPaintingDialog();
        }
    };

    private AdapterView.OnItemClickListener rowEventListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Painting painting = paintingsArrayList.get(position);
            Intent detailIntent = new Intent(getApplicationContext(), PaintingDetailActivity.class);
            detailIntent.putExtra("title", painting.getTitle());
            detailIntent.putExtra("artist", painting.getArtist());
            detailIntent.putExtra("year", Integer.toString(painting.getYear()));
            detailIntent.putExtra("description", painting.getDescription());
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
        addBtn.setOnClickListener(addPaintingDialogEventListener);

        SqLiteHelper = new SQLiteHelper(getApplicationContext());
        allPaintings = SqLiteHelper.getAllPaintings();

        paintingsListView = findViewById(R.id.paintings_list_view);
        paintingsListView.setOnItemClickListener(rowEventListener);
        paintingsArrayList = new ArrayList<>(allPaintings);
        sortRows(0);
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
        // TODO: add these dialog strings
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
        // TODO: refactor all dialogs to one liners
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

    private void buildAddPaintingDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View inflatorView = inflater.inflate(R.layout.add_painting_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.currency_dialog_title)
                .setView(inflatorView)
                .setPositiveButton("Add painting", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean formValid = false;
                        EditText titleField = inflatorView.findViewById(R.id.edit_painting_title);
                        EditText artistField = inflatorView.findViewById(R.id.edit_painting_artist);
                        EditText yearField = inflatorView.findViewById(R.id.edit_painting_year);
                        EditText roomField = inflatorView.findViewById(R.id.edit_painting_room);
                        EditText descField = inflatorView.findViewById(R.id.edit_painting_description);
                        EditText rankField = inflatorView.findViewById(R.id.edit_painting_rank);

                        String artist = artistField.getText().toString();
                        String title = titleField.getText().toString();
                        String room = roomField.getText().toString();
                        String desc = descField.getText().toString();
                        int year = Integer.parseInt(yearField.getText().toString());
                        int rank = Integer.parseInt(rankField.getText().toString());

                        if (artist.length() > 0 && title.length() > 0 && Integer.toString(year).length() == 4) {
                            formValid = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Your form has an error.", Toast.LENGTH_LONG).show();
                        }

                        if (formValid) {
                            Painting painting = new Painting(artist, title, room, desc, "", year, rank);
                            SqLiteHelper.addPainting(painting);
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Painting successfully added", Toast.LENGTH_LONG).show();

                            paintingsArrayList.add(painting);
                            sortRows(0);
                        }
                    }
                });
            }
        });
        dialog.show();
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
