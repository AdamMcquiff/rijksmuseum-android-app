package com.cet325.bg72db;

import com.cet325.bg72db.PaintingListView.PaintingUtils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PaintingUtilsUnitTests {

    @Test
    public void testFormValidatorValidatesFormCorrectlyWithValidData() {
        // Initialise PaintingUtils class
        PaintingUtils utils = new PaintingUtils();

        // Setup test data
        String artist = "Example Artist";
        String title = "Example title";
        int year = 1996;
        int rank = 4;

        // Assert form validation methods returns empty array when provided valid data
        assertEquals(
                Arrays.toString(utils.getFormErrors(artist, title, year, rank)),
                Arrays.toString(new String[0])
        );
    }

    @Test
    public void testFormValidatorValidatesFormCorrectlyWithInvalidData() {
        // Initialise PaintingUtils class
        PaintingUtils utils = new PaintingUtils();

        // Setup test data
        String title = "";
        String artist = "Example artist";
        int year = 60001;
        int rank = 6;

        // Setup errors array
        String[] errors = new String[4];
        errors[0] = "Painting Title is required.";

        // Assert form validation method returns errors array when provided invalid data
        assertEquals(
                Arrays.toString(utils.getFormErrors(artist, title, year, rank)),
                Arrays.toString(errors)
        );
    }

}