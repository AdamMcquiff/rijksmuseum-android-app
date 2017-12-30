package com.cet325.bg72db.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cet325.bg72db.SQLite.Models.Painting;

import java.util.LinkedList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

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
    private static final String KEY_ADDED_BY = "added_by";

    private static final String[] COLUMNS = {
        KEY_ID, KEY_ARTIST, KEY_TITLE, KEY_ROOM, KEY_DESCRIPTION, KEY_IMAGE, KEY_YEAR, KEY_RANK, KEY_ADDED_BY
    };

    public SQLiteHelper(Context context) {
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
            "rank INT, " +
            "added_by TEXT "+
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
        values.put(KEY_ADDED_BY, painting.getAddedBy());

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
            painting.setYear(cursor.getInt(6));
            painting.setRank(cursor.getInt(7));
            painting.setAddedBy(cursor.getString(8));
        }
        cursor.close();
        return painting;
    }

    public List<Painting> getAllPaintings() {
        List<Painting> paintings = new LinkedList<>();

        String query = "SELECT  * FROM " + TABLE_PAINTINGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            Painting painting;
            do {
                painting = new Painting();
                painting.setId(Integer.parseInt(cursor.getString(0)));
                painting.setArtist(cursor.getString(1));
                painting.setTitle(cursor.getString(2));
                painting.setRoom(cursor.getString(3));
                painting.setDescription(cursor.getString(4));
                painting.setYear(cursor.getInt(6));
                painting.setRank(cursor.getInt(7));
                painting.setAddedBy(cursor.getString(8));
                paintings.add(painting);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return paintings;
    }

    public int updatePainting(Painting painting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST, painting.getArtist());
        values.put(KEY_TITLE, painting.getTitle());
        values.put(KEY_ROOM, painting.getRoom());
        values.put(KEY_DESCRIPTION, painting.getDescription());
        values.put(KEY_IMAGE, painting.getImage());
        values.put(KEY_YEAR, painting.getYear());
        values.put(KEY_RANK, painting.getRank());
        values.put(KEY_ADDED_BY, painting.getAddedBy());

        int i = db.update(
            TABLE_PAINTINGS,
            values,
            KEY_ID+" = ?",
            new String[] { String.valueOf(painting.getId()) }
        );

        db.close();
        return i;
    }

    public void deletePainting(Painting painting) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
            TABLE_PAINTINGS,
            KEY_ID+" = ?",
            new String[] { String.valueOf(painting.getId()) }
        );

        db.close();
    }

    public Painting getPaintingByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
            TABLE_PAINTINGS,
            COLUMNS,
            KEY_TITLE + " = ?",
            new String[] { title },
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
            painting.setYear(cursor.getInt(6));
            painting.setRank(cursor.getInt(7));
            painting.setAddedBy(cursor.getString(8));
        }

        cursor.close();
        return painting;
    }

}
