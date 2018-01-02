package com.cet325.bg72db.SQLite.Models;

public class Painting {

    private int id;
    private String artist;
    private String title;
    private String room;
    private String description;
    private byte[] image;
    private int year;
    private int rank;
    private String addedBy;

    public Painting() {}

    public Painting(String artist, String title, String room, String description, byte[] image, int year, int rank, String addedBy) {
        this.artist = artist;
        this.title = title;
        this.room = room;
        this.description = description;
        this.image = image;
        this.year = year;
        this.rank = rank;
        this.addedBy = addedBy;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getAddedBy() {
        return addedBy;
    }
    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    @Override
    public String toString() {
        return title + " by " + this.artist + ". Complete " + this.year + ".";
    }

}
