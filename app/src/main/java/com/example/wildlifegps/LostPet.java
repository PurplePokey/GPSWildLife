package com.example.wildlifegps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.wildlifegps.DBHandler.animalIDCount;
import static com.example.wildlifegps.DBHandler.sightingIDCount;

public class LostPet extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LostPet.this;

    private LocationManager locationManager;
    private MyLocationListener mylistener;

    private EditText type;
    private EditText name;
    private EditText desc;

    private Intent i;
    private String user;

    private double latitude;
    private double longitude;
    private Location location;


    private Button cancel;
    private Button create;

    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_pet);

        getSupportActionBar().hide();
        i =getIntent();
        user= i.getExtras().getString("user");

        initViews();
        initListeners();
        initObjects();

    }
    private void initViews(){
        type=(EditText) findViewById(R.id.type_pet_lost);
        name = (EditText) findViewById(R.id.name_pet_lost);
        desc = (EditText) findViewById(R.id.desc_pet_lost);

        cancel = (Button) findViewById(R.id.cancel_pet_lost);
        create= (Button) findViewById(R.id.create_pet_lost);
    }

    private void initListeners(){
        cancel.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    private void initObjects(){
        db=new DBHandler(activity);
        mylistener = new MyLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            }
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1, mylistener);
            if(locationManager!=null){
                location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
    }

    @Override
    public void onClick(View view){
        //upload image
        if(view.getId()==(R.id.upload_image)){

        }

        //if create sighting button is pressed
        if(view.getId()==(R.id.create_pet_lost)){
            //validate title field is filled if request switch is off
            if(checkInput(type)){
                //validate desc field is filled
                if(checkInput(name)){

                    addLostPet();
                    finish();

                }
                //popup "please enter a description"
                else{

                }
            }
            //give pop up "request identification or enter a title"
            else{

            }
        }
        if(view.getId()==(R.id.cancel_pet_lost)) {
            finish();
        }

    }

    private void addLostPet(){
        Sighting sighting =new Sighting();

        //get information to create sighting object
        String strTitle = type.getText().toString().trim();
        int id = sightingIDCount;
        User owner = new User(user);
        Pet pet = new Pet (animalIDCount, strTitle);
        pet.setLostFound(0);
        db.addAnimal(pet);
        Calendar calendar = Calendar.getInstance();
        String description = desc.getText().toString().trim();

        sighting.setTitle(strTitle);
        sighting.setID(id);
        sighting.setOwner(owner);
        sighting.setAnimal(pet);
        sighting.setLocation(location);
        sighting.setTimestamp(calendar);
        sighting.setDescription(description);


        db.addSighting(sighting);

        }


    private boolean checkInput(EditText text){
        String value = text.getText().toString().trim();
        if(value.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    private class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location){
            latitude=location.getLatitude();
            longitude= location.getLongitude();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
        }
        @Override
        public void onProviderEnabled(String provider){
        }
        @Override
        public void onProviderDisabled(String provider){
        }
    }
}
