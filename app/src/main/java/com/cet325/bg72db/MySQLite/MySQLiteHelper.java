package com.cet325.bg72db.MySQLite;

import android.content.ContentValues;
import android.content.Context;
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

}
