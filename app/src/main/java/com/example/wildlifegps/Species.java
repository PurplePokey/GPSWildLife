package com.example.wildlifegps;

public class Species extends Animal{

    private String scienceName;
    private String conserveStatus;
    private String diet;
    private String appearance;

    Species(int animalID, String commonName, String scienceName, String conserveStatus, String diet, String appearance){
        super(animalID, commonName);
        this.scienceName=scienceName;
        this.conserveStatus=conserveStatus;
        this.diet=diet;
        this.appearance=appearance;
    }

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public String getConserveStatus() {
        return conserveStatus;
    }

    public void setConserveStatus(String conserveStatus) {
        this.conserveStatus = conserveStatus;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }
}
