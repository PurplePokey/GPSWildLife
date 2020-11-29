package com.example.wildlifegps;

import java.util.ArrayList;

public abstract class Animal {

    private int animalID;
    private String commonName;
    private ArrayList<User> observers;

    Animal(int animalID, String commonName){
        this.animalID=animalID;
        this.commonName=commonName;
        observers = new ArrayList<>();
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public ArrayList<User> getObservers() {
        return observers;
    }

    public void addObserver(User user){
        observers.add(user);
    }

    public void removeObserver(User user){
        observers.remove(user);
    }

    public void setObservers(ArrayList<User> observers) {
        this.observers = observers;
    }
}
