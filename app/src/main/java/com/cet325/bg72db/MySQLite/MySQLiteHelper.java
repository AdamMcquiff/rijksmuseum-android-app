package com.cet325.bg72db.MySQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cet325.bg72db.MySQLite.Models.Painting;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MuseumDB";
    private static final String TABLE_PAINTINGS = "paintings";

    private static final String KEY_ID = "id";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ROOM = "room";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_YEAR = "year";
    private static final String KEY_RANK = "rank";

    private static final String[] COLUMNS = {
        KEY_ID, KEY_ARTIST, KEY_TITLE, KEY_ROOM, KEY_DESCRIPTION, KEY_IMAGE, KEY_YEAR, KEY_RANK
    };

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PAINTINGS_TABLE = "CREATE TABLE paintings (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "artist TEXT, "+
            "title TEXT, "+
            "room TEXT, "+
            "description TEXT, "+
            "image TEXT, "+
            "year INT, "+
            "rank INT " +
        ")";
        db.execSQL(CREATE_PAINTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS paintings");
        this.onCreate(db);
    }

    public void addPainting(Painting painting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST, painting.getArtist());
        values.put(KEY_TITLE, painting.getTitle());
        values.put(KEY_ROOM, painting.getRoom());
        values.put(KEY_DESCRIPTION, painting.getDescription());
        values.put(KEY_IMAGE, painting.getImage());
        values.put(KEY_YEAR, painting.getYear());
        values.put(KEY_RANK, painting.getRank());

        db.insert(TABLE_PAINTINGS, null, values);
        db.close();
    }

    public Painting getPainting(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
            TABLE_PAINTINGS,
            COLUMNS,
            " id = ?",
            new String[] { String.valueOf(id) },
            null,
            null,
            null,
            null
        );

        Painting painting = null;
        if (cursor != null && cursor.moveToFirst()) {
            painting = new Painting();
            painting.setId(Integer.parseInt(cursor.getString(0)));
            painting.setArtist(cursor.getString(1));
            painting.setTitle(cursor.getString(2));
            painting.setRoom(cursor.getString(3));
            painting.setDescription(cursor.getString(4));
            painting.setYear(cursor.getInt(5));
            painting.setRank(cursor.getInt(6));
            Log.d("getPainting(" + id + ")", painting.toString());
        }
        cursor.close();
        return painting;
    }

    private void populateDatabase() {
        addPainting(new Painting("Caesar Boëtius van Everdingen", "Willem Jacobsz Baert (1636-84), Burgomaster of Alkmaar and Amsterdam", "", "Portret van Willem Jacobsz Baert, burgemeester van Alkmaar en Amsterdam. Ten halven lijve, staande met een handschoen in de linkerhand. Pendant van SK-A-1340.", "", 1671, 3));
        addPainting(new Painting("Bartolommeo Vivarini", "Saint Cosmas (or Damian)", "", "Halffiguur van de heilige Cosmas (of misschien Damianus), in de hand een vierkante zalfdoos. Pendant van SK-A-4012.", "", 1460, 3));
        addPainting(new Painting("Maarten van Heemskerck", "Portraits of a Couple", "", "Portret van een vrouw, vroeger geïdentificeerd als Anna Codde, de echtgenote van Pieter Gerritsz Bicker. Zittend, ten halven lijve, spinnend aan een spinnewiel. Pendant van SK-A-3518.", "", 1529, 4));
        addPainting(new Painting("George Hendrik Breitner", "Ginger Pot wit Anemones", "", "Stilleven van een gemberpot met anemonen.", "", 1923, 3));
        addPainting(new Painting("Abraham Teerlink", "Staande vrouw in Italiaanse klederdracht", "", "--", "", 1857, 3));
        addPainting(new Painting("Johan Braakensiek", "Zittend meisje in klederdracht", "", "--", "", 1940, 3));
        addPainting(new Painting("Johannes Frederik Engelbert ten Klooster", "Dalang, Johannes Frederik Engelbert ten Klooster", "", "--", "", 1928, 3));
        addPainting(new Painting("Yashima Gakutei", "Dansende geisha's, Yashima Gakutei", "", "Twee geisha's dansen op een podium met lantarens boven hun hoofd, met in de achtergrond kersenbloesems en pijnbomen. Dit is het rechterblad van het vijfluik. Met twee gedichten.", "", 1824, 3));
        addPainting(new Painting("Tokuriki Tomikichirô", "Vergezicht op de berg Fuji vanaf het station Kiyosato in Nagano", "", "Een bergketen waarachter de top van de besneeuwde berg Fuji; op de voorgrond een vlakte met voornamelijk kale bomen.", "", 1940, 5 ));
        addPainting(new Painting("Thomas James Dixon", "Tijger in een dierentuin", "", "--", "", 1880, 4));
    }

}
