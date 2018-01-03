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

import com.cet325.bg72db.PaintingListView.PaintingAdapter;
import com.cet325.bg72db.PaintingListView.PaintingUtils;
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
    PaintingUtils paintingUtils = null;

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
            // Only send ID and title to intent, as detail activity will retrieve from db
            // using one of these values
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

        sortBtn = (FloatingActionButton) findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(sortRowsEventListener);
        filterBtn = (FloatingActionButton) findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(filterRowsEventListener);
        addBtn = (FloatingActionButton) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(addPaintingDialogEventListener);

        SqLiteHelper = new SQLiteHelper(getApplicationContext());
        allPaintings = SqLiteHelper.getAllPaintings();

        paintingUtils = new PaintingUtils();

        paintingsListView = (ListView) findViewById(R.id.paintings_list_view);
        paintingsListView.setOnItemClickListener(rowEventListener);
        paintingsArrayList = new ArrayList<>(allPaintings);
        reorderListViewRows(selectedSortCriteria);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure painting has been added to list after AddPainting dialog interaction,
        // in event that painting has been added
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

        // Add separate listener for button positive click to stop dialog from closing in the event
        // that the form is not valid, which would be the case if a listener was added on the
        // AlertDialog Builder setPositiveButton method.
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get form fields and store in variables
                        EditText titleField = (EditText) inflaterView.findViewById(R.id.edit_painting_title);
                        EditText artistField = (EditText) inflaterView.findViewById(R.id.edit_painting_artist);
                        EditText yearField = (EditText) inflaterView.findViewById(R.id.edit_painting_year);
                        EditText roomField = (EditText) inflaterView.findViewById(R.id.edit_painting_room);
                        EditText descField = (EditText) inflaterView.findViewById(R.id.edit_painting_description);
                        EditText rankField = (EditText) inflaterView.findViewById(R.id.edit_painting_rank);

                        // Get data from form fields and store in variables
                        String artist = artistField.getText().toString();
                        String title = titleField.getText().toString();
                        String room = roomField.getText().toString();
                        String desc = descField.getText().toString();
                        int year = yearField.length() > 0 ? Integer.parseInt(yearField.getText().toString()) : 0;
                        int rank = rankField.length() > 0 ? Integer.parseInt(rankField.getText().toString()) : 0;

                        // Check form for errors
                        String[] formErrors = paintingUtils.getFormErrors(artist, title, year, rank);

                        // If no errors, store painting in db, add to list view array list, reorder list view
                        // to compensate for new item, show Toast notification to user, close dialog.
                        if (formErrors.length == 0) {
                            Painting painting = new Painting(artist, title, room, desc, null, year, rank, "User");
                            SqLiteHelper.addPainting(painting);
                            paintingsArrayList.add(painting);
                            reorderListViewRows(selectedSortCriteria);
                            Toast.makeText(getApplicationContext(), "Painting successfully added", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            // If error, loop through errors and present to user via Toast notifications
                            for (String formError : formErrors) {
                                if (formError != null)
                                    Toast.makeText(getApplicationContext(), formError, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void filterRows(int selected) {
        // Initialise paintings ArrayList to temp store filtered paintings
        List<Painting> paintings = new ArrayList<>();

        // Reset paintingArrayList in case of previous filters
        paintingsArrayList = new ArrayList<>(allPaintings);
        switch (selected) {
            // No filtering, simply use fresh array list and save user preference.
            case 0:
                selectedFilterCriteria = 0;
                break;
            // Filter to ranked; save user preference; loop array and check rank,
            // store ranked in paintings array list and reinitialise paintingsArrayList.
            case 1:
                selectedFilterCriteria = 1;
                for (int i = 0; i < paintingsArrayList.size(); i++) {
                    Painting painting = paintingsArrayList.get(i);
                    if (painting.getRank() != 0) paintings.add(painting);
                }
                paintingsArrayList = new ArrayList<>(paintings);
                break;
            // Filter to un-ranked; save user preference; loop array and check rank,
            // store un-ranked in paintings array list and reinitialise paintingsArrayList.
            case 2:
                selectedFilterCriteria = 2;
                for (int i = 0; i < paintingsArrayList.size(); i++) {
                    Painting painting = paintingsArrayList.get(i);
                    if (painting.getRank() == 0) paintings.add(painting);
                }
                paintingsArrayList = new ArrayList<>(paintings);
                break;
        }

        // Reorder ListView rows using previously selected user sort criteria; this
        // will also reinitialise the list view adapter.
        reorderListViewRows(selectedSortCriteria);
    }

    private void reorderListViewRows(int selected) {
        // Switch statement; save user preference; sort collection as appropriate
        switch (selected) {
            // Sort by title
            case 0:
                selectedSortCriteria = 0;
                Collections.sort(paintingsArrayList, new Comparator<Painting>(){
                    public int compare(Painting a, Painting b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            // Sort by artist, title
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
            // Sort by rank
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
