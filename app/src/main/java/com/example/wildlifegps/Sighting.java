package com.example.wildlifegps;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class Sighting implements Serializable {
    private String title;
    private int ID;
    private User owner;
    private Animal animal;
    private SerialLoc location;
    private Calendar timestamp;
    private String description;
//    private byte imageFileName[];
    private int flagCount;
    private Uri imageFileName;
//
//
//    private Bitmap image;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
//
//    public Bitmap getImage() {
//        return image;
//    }
//
//    public void setImage(Bitmap image) {
//        this.image = image;
//    }

    public Uri getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(Uri imageFileName) {
        this.imageFileName = imageFileName;
    }
    public Sighting(){
        owner=new User();
    }
    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location.toRealLoc();
    }

    public void setLocation(Location location) {
        this.location = new SerialLoc(location.getLatitude(), location.getLongitude());
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//
//    public byte[] getImageFileName() {
//        return imageFileName;
//    }
//
//    public void setImageFileName(byte[] imageFileName) {
//        this.imageFileName = new byte[imageFileName.length];
//        for(int i = 0 ; i<imageFileName.length;i++){
//            this.imageFileName[i]=imageFileName[i];
//        }
//
//    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags.addAll(tags);
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


}
