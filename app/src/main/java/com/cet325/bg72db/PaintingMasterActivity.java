package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    PaintingAdapter adapter = null;

    int selectedSortCriteria = 0;
    int selectedFilterCriteria = 0;

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
            Intent intent = new Intent(getApplicationContext(), PaintingDetailActivity.class);
            intent.putExtra("id", painting.getId());
            intent.putExtra("title", painting.getTitle());
            startActivity(intent);
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
        reorderListViewRows(selectedSortCriteria);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allPaintings = SqLiteHelper.getAllPaintings();
        paintingsArrayList = new ArrayList<>(allPaintings);
        reorderListViewRows(selectedSortCriteria);
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
            case R.id.find_us:
                Intent findUsActivityIntent = new Intent(getApplicationContext(), FindUsActivity.class);
                startActivity(findUsActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buildRowSortDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.master_painting_sort_dialog_title)
                .setItems(R.array.sortable_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selected) {
                        reorderListViewRows(selected);
                    }
                })
                .create()
                .show();
    }

    private void buildRowFilterDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.master_painting_filter_dialog_title)
            .setItems(R.array.filter_options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int selected) {
                    filterRows(selected);
                }
            })
            .create()
            .show();
    }

    private void buildAddPaintingDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View inflaterView = inflater.inflate(R.layout.add_painting_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.master_painting_add_dialog_title)
                .setView(inflaterView)
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
                        EditText titleField = inflaterView.findViewById(R.id.edit_painting_title);
                        EditText artistField = inflaterView.findViewById(R.id.edit_painting_artist);
                        EditText yearField = inflaterView.findViewById(R.id.edit_painting_year);
                        EditText roomField = inflaterView.findViewById(R.id.edit_painting_room);
                        EditText descField = inflaterView.findViewById(R.id.edit_painting_description);
                        EditText rankField = inflaterView.findViewById(R.id.edit_painting_rank);

                        String artist = artistField.getText().toString();
                        String title = titleField.getText().toString();
                        String room = roomField.getText().toString();
                        String desc = descField.getText().toString();
                        int year = yearField.length() > 0 ? Integer.parseInt(yearField.getText().toString()) : 0;
                        int rank = rankField.length() > 0 ? Integer.parseInt(rankField.getText().toString()) : 0;

                        if (isFormValid(artist, title, year, rank)) {
                            Painting painting = new Painting(artist, title, room, desc, null, year, rank, "User");
                            SqLiteHelper.addPainting(painting);
                            paintingsArrayList.add(painting);
                            reorderListViewRows(selectedSortCriteria);
                            Toast.makeText(getApplicationContext(), "Painting successfully added", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private boolean isFormValid(String artist, String title, int year, int rank) {
        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(), "Painting Title is required.", Toast.LENGTH_LONG).show();
            return false;
        } else if (artist.length() == 0) {
            Toast.makeText(getApplicationContext(), "Painting Artist is required.", Toast.LENGTH_LONG).show();
            return false;
        }else if (Integer.toString(year).length() < 4) {
            Toast.makeText(getApplicationContext(), "Painting Year is required and must be 4 digits.", Toast.LENGTH_LONG).show();
            return false;
        } else if (rank < 0 || rank > 5) {
            Toast.makeText(getApplicationContext(), "Painting Rank must be between 0 and 5. Please enter 0 or leave empty if un-ranked.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void filterRows(int selected) {
        List<Painting> paintings = new ArrayList<>();
        paintingsArrayList = new ArrayList<>(allPaintings);
        Log.d("selected", Integer.toString(selected));
        switch (selected) {
            case 0:
                selectedFilterCriteria = 0;
                break;
            case 1:
                selectedFilterCriteria = 1;
                for (int i = 0; i < paintingsArrayList.size(); i++) {
                    Painting painting = paintingsArrayList.get(i);
                    if (painting.getRank() != 0) paintings.add(painting);
                }
                paintingsArrayList = new ArrayList<>(paintings);
                break;
            case 2:
                selectedFilterCriteria = 2;
                for (int i = 0; i < paintingsArrayList.size(); i++) {
                    Painting painting = paintingsArrayList.get(i);
                    if (painting.getRank() == 0) paintings.add(painting);
                }
                paintingsArrayList = new ArrayList<>(paintings);
                break;
        }
        reorderListViewRows(selectedSortCriteria);
    }

    private void reorderListViewRows(int selected) {
        switch (selected) {
            case 0:
                selectedSortCriteria = 0;
                Collections.sort(paintingsArrayList, new Comparator<Painting>(){
                    public int compare(Painting a, Painting b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case 1:
                selectedSortCriteria = 1;
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
                selectedSortCriteria = 2;
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

    private void setupRowAdapter() {
        adapter = new PaintingAdapter(this, paintingsArrayList);
        paintingsListView.setAdapter(adapter);
    }

}
