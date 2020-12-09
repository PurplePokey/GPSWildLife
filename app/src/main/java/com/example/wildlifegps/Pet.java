package com.example.wildlifegps;

import java.io.Serializable;

public class Pet extends Animal implements Serializable {

    private int petID;
    private String petName;
    //0 for lost 1 for found
    private int lostFound;

    Pet(int animalID, String commonName, int petID, String petName){
        super(animalID, commonName);
        this.petID=petID;
        this.petName=petName;
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getLostFound() {
        return lostFound;
    }

    public void setLostFound(int lostFound) {
        this.lostFound = lostFound;
    }


}
