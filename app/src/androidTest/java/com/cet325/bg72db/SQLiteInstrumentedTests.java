package com.cet325.bg72db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cet325.bg72db.SQLite.Models.Painting;
import com.cet325.bg72db.SQLite.SQLiteHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SQLiteInstrumentedTests {

    private SQLiteHelper db;
    private Painting painting;

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.cet325.bg72db", appContext.getPackageName());
    }

    @Before
    public void setUp() throws Exception {
        db = new SQLiteHelper(InstrumentationRegistry.getTargetContext());
        painting = new Painting("Artist", "Title", "Room", "Description", "", 1671, 0, "App");
    }

    @After
    public void finish() throws Exception {
        db.close();
    }

    @Test
    public void testCanAddPaintingToDB() {
        // Ensure DB is empty before beginning tests
        db.deleteAllPaintings();

        // Add painting to DB
        db.addPainting(painting);

        // Check total DB paintings is 1
        assertEquals(db.getAllPaintings().size(), 1);
    }

    @Test
    public void testCanGetPaintingFromDBByTitle() {
        // Ensure DB is empty before beginning tests
        db.deleteAllPaintings();

        // Add painting to DB
        db.addPainting(painting);

        // Attempt to get painting by title
        Painting newPainting = db.getPaintingByTitle(painting.getTitle());

        // Confirm the paintings match
        assertEquals(painting.getTitle(), newPainting.getTitle());
    }

    @Test
    public void testCanDeletePaintingFromDB() {
        // Ensure DB is empty before beginning tests
        db.deleteAllPaintings();

        // Add painting to DB
        db.addPainting(painting);

        // Set the painting id so that it can be removed from DB
        painting.setId(db.getPaintingByTitle(painting.getTitle()).getId());

        // Attempt to delete painting from DB
        db.deletePainting(painting);

        // Check total DB paintings is 0
        assertEquals(db.getAllPaintings().size(), 0);
    }

}
