package com.cet325.bg72db;

import android.annotation.SuppressLint;
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

import com.cet325.bg72db.PaintingListView.PaintingUtils;
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

    PaintingUtils paintingUtils = null;

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

        editBtn = (FloatingActionButton) findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(editPaintingDialogEventListener);

        sqLiteHelper = new SQLiteHelper(getApplicationContext());

        // Get painting by title. Used this instead of ID as if the painting has just been added
        // by the user, the id will not be accessible.
        painting = sqLiteHelper.getPaintingByTitle(this.getIntent().getExtras().getString("title"));

        paintingUtils = new PaintingUtils();

        paintingImage = (ImageView) findViewById(R.id.painting_image);
        paintingTitle = (TextView) findViewById(R.id.painting_title);
        paintingArtist = (TextView) findViewById(R.id.painting_artist);
        paintingYear = (TextView) findViewById(R.id.painting_year);
        paintingDescription = (TextView) findViewById(R.id.painting_description);
        paintingRoom = (TextView) findViewById(R.id.painting_room);
        paintingRank = (TextView) findViewById(R.id.painting_rank);

        updateTextFields();

        deletePaintingBtn = (Button) findViewById(R.id.delete_painting_btn);

        // If painting added by user, show the 'Delete Painting' button
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
            case R.id.paintings_link:
                onBackPressed();
                return true;
            case R.id.find_us:
                Intent findUsActivityIntent = new Intent(getApplicationContext(), FindUsActivity.class);
                startActivity(findUsActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    private void buildEditPaintingDialog() {
        // Setup Edit Painting dialog and store EditText fields into variables.
        LayoutInflater inflater = LayoutInflater.from(this);
        final View inflaterView = inflater.inflate(R.layout.add_painting_dialog, null);
        final EditText titleField = (EditText) inflaterView.findViewById(R.id.edit_painting_title);
        final EditText artistField = (EditText) inflaterView.findViewById(R.id.edit_painting_artist);
        final EditText descField = (EditText) inflaterView.findViewById(R.id.edit_painting_description);
        final EditText roomField = (EditText) inflaterView.findViewById(R.id.edit_painting_room);
        final EditText yearField = (EditText) inflaterView.findViewById(R.id.edit_painting_year);
        final EditText rankField = (EditText) inflaterView.findViewById(R.id.edit_painting_rank);

        // if Painting was not added by user, disable relevant fields.
        if (painting.getAddedBy().equals("App")) {
            artistField.setEnabled(false);
            titleField.setEnabled(false);
            descField.setEnabled(false);
            roomField.setEnabled(false);
            yearField.setEnabled(false);
        }

        // Populate EditText fields with pre-existing painting data.
        titleField.setText(painting.getTitle());
        artistField.setText(painting.getArtist());
        descField.setText(painting.getDescription());
        roomField.setText(painting.getRoom());
        yearField.setText(Integer.toString(painting.getYear()));
        rankField.setText(Integer.toString(painting.getRank()));

        // Build dialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.detail_painting_edit_dialog_title)
                .setView(inflaterView)
                .setPositiveButton("Confirm changes", null)
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
                        // Get form data
                        String artist = artistField.getText().toString();
                        String title = titleField.getText().toString();
                        String room = roomField.getText().toString();
                        String desc = descField.getText().toString();
                        int year = yearField.length() > 0 ? Integer.parseInt(yearField.getText().toString()) : 0;
                        int rank = rankField.length() > 0 ? Integer.parseInt(rankField.getText().toString()) : 0;

                        // Check form data for errors
                        String[] formErrors = paintingUtils.getFormErrors(artist, title, year, rank);

                        // If no errors, update painting, update activity,
                        // send Toast notification and close dialog
                        if (formErrors.length == 0) {
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
                        } else {
                            // If errors, loop through and present to user via Toast notification
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
