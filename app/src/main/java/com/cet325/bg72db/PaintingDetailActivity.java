package com.cet325.bg72db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cet325.bg72db.SQLite.Models.Painting;
import com.cet325.bg72db.SQLite.SQLiteHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PaintingDetailActivity extends AppCompatActivity {

    ActionBar actionBar = null;
    FloatingActionButton editBtn = null;
    ImageView paintingImage = null;
    TextView paintingTitle = null;
    TextView paintingArtist = null;
    TextView paintingYear = null;
    TextView paintingDescription = null;
    TextView paintingRoom = null;
    TextView paintingRank = null;
    Button deletePaintingBtn = null;

    SQLiteHelper sqLiteHelper = null;
    Painting painting = null;

    private View.OnClickListener editPaintingDialogEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            buildEditPaintingDialog();
        }
    };

    private View.OnClickListener deletePaintingDialogEventListener = new View.OnClickListener() {
        public void onClick(View view) {
            buildDeletePaintingDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_detail);

        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(editPaintingDialogEventListener);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());

        painting = sqLiteHelper.getPaintingByTitle(this.getIntent().getExtras().getString("title"));

        paintingImage = findViewById(R.id.painting_image);
        paintingTitle = findViewById(R.id.painting_title);
        paintingArtist = findViewById(R.id.painting_artist);
        paintingYear = findViewById(R.id.painting_year);
        paintingDescription = findViewById(R.id.painting_description);
        paintingRoom = findViewById(R.id.painting_room);
        paintingRank = findViewById(R.id.painting_rank);

        updateTextFields();

        deletePaintingBtn = findViewById(R.id.delete_painting_btn);
        if (painting.getAddedBy().equals("User")) {
            deletePaintingBtn.setVisibility(View.VISIBLE);
            deletePaintingBtn.setOnClickListener(deletePaintingDialogEventListener);
        }
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

    private void buildEditPaintingDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View inflaterView = inflater.inflate(R.layout.add_painting_dialog, null);
        final EditText titleField = inflaterView.findViewById(R.id.edit_painting_title);
        final EditText artistField = inflaterView.findViewById(R.id.edit_painting_artist);
        final EditText descField = inflaterView.findViewById(R.id.edit_painting_description);
        final EditText roomField = inflaterView.findViewById(R.id.edit_painting_room);
        final EditText yearField = inflaterView.findViewById(R.id.edit_painting_year);
        final EditText rankField = inflaterView.findViewById(R.id.edit_painting_rank);

        // if Painting was not added by user, disable relevant fields.
        if (painting.getAddedBy().equals("App")) {
            artistField.setEnabled(false);
            titleField.setEnabled(false);
            descField.setEnabled(false);
            roomField.setEnabled(false);
            yearField.setEnabled(false);
        }

        titleField.setText(painting.getTitle());
        artistField.setText(painting.getArtist());
        descField.setText(painting.getDescription());
        roomField.setText(painting.getRoom());
        yearField.setText(Integer.toString(painting.getYear()));
        rankField.setText(Integer.toString(painting.getRank()));

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.detail_painting_edit_dialog_title)
                .setView(inflaterView)
                .setPositiveButton("Confirm changes", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String artist = artistField.getText().toString();
                        String title = titleField.getText().toString();
                        String room = roomField.getText().toString();
                        String desc = descField.getText().toString();
                        int year = 0;
                        int rank = 0;
                        if (yearField.length() > 0) year = Integer.parseInt(yearField.getText().toString());
                        if (rankField.length() > 0) rank = Integer.parseInt(rankField.getText().toString());

                        if (isFormValid(artist, title, year, rank)) {
                            painting.setArtist(artist);
                            painting.setTitle(title);
                            painting.setRoom(room);
                            painting.setDescription(desc);
                            painting.setYear(year);
                            painting.setRank(rank);
                            sqLiteHelper.updatePainting(painting);
                            updateTextFields();
                            Toast.makeText(getApplicationContext(), "Painting successfully edited", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), "Painting Rank must be between 0 and 5. Please enter 0 if un-ranked.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void buildDeletePaintingDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.detail_painting_delete_dialog_title)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sqLiteHelper.deletePainting(painting);
                        Toast.makeText(getApplicationContext(), "Painting successfully deleted", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateTextFields() {
        if (painting.getImage() != null) {
            try {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File f = new File(dir, painting.getImage());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap image = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                paintingImage.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        paintingTitle.setText(painting.getTitle());
        paintingArtist.setText(painting.getArtist());
        paintingDescription.setText(painting.getDescription());
        paintingRoom.setText(getString(R.string.detail_painting_room, painting.getRoom()));
        paintingYear.setText(getString(R.string.detail_painting_year, Integer.toString(painting.getYear())));
        paintingRank.setText(getString(R.string.detail_painting_rank, Integer.toString(painting.getRank())));
    }
}
