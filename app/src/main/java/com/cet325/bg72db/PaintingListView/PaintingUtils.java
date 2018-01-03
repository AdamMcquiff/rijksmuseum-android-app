package com.cet325.bg72db.PaintingListView;

public class PaintingUtils {

    /**
     * This method takes a handful of required fields, validates them and returns any errors
     * to the application. The application can then check if the array is empty to know if
     * the form is valid, otherwise they can present the errors to the user.
     *
     * Method will only return one error at a time.
     *
     * @param artist the painting artist.
     * @param title the painting title.
     * @param year the year the painting was complete.
     * @param rank the users rank of the painting.
     * @return an array of errors to display to the user, empty array is returned if no errors.
     */
    public String[] getFormErrors(String artist, String title, int year, int rank) {
        String[] errors = new String[4];
        if (title.length() == 0) {
            errors[0] = "Painting Title is required.";
        } else if (artist.length() == 0) {
            errors[1] = "Painting Artist is required.";
        } else if (Integer.toString(year).length() < 4) {
            errors[2] = "Painting Year is required and must be 4 digits.";
        } else if (rank < 0 || rank > 5) {
            errors[3] = "Painting Rank must be between 0 and 5. Please enter 0 or leave empty if un-ranked.";
        }

        for (String error : errors) {
            if (error != null) {
                return errors;
            }
        }
        return new String[0];
    }

}
