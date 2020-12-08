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

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.wildlifegps.DBHandler.animalIDCount;
import static com.example.wildlifegps.DBHandler.sightingIDCount;

public class CreateSighting extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = CreateSighting.this;

    private LocationManager locationManager;
    private MyLocationListener mylistener;

    private EditText title;
    private EditText desc;
    private EditText tags;

    private Switch identify;

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
        setContentView(R.layout.sighting_create);

        getSupportActionBar().hide();
        i =getIntent();
        user= i.getExtras().getString("user");

        initViews();
        initListeners();
        initObjects();

    }

    private void initViews(){
        title=(EditText) findViewById(R.id.sighting_title_create);
        desc = (EditText) findViewById(R.id.sighting_desc_create);
        tags = (EditText) findViewById(R.id.sighting_tags_create);

        identify=(Switch) findViewById(R.id.requestIdenSwitch);

        cancel = (Button) findViewById(R.id.cancel_create_sighting);
        create= (Button) findViewById(R.id.create_sighting);
    }

    private void initListeners(){
        cancel.setOnClickListener(this);
        create.setOnClickListener(this);

        identify.setOnClickListener(this);
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
        //boolean sw = false;

        //if create sighting button is pressed
        if(view.getId()==(R.id.create_sighting)){
            //get status of Switch
            //sw = identify.isChecked();

            //validate title field is filled if request switch is off
            if(checkInput(title)/*&&!sw*/){
                //validate desc field is filled
                if(checkInput(desc)){

                    addSighting();
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
        if(view.getId()==(R.id.cancel_create_sighting)) {
            finish();
        }

    }

    private void addSighting(){
        Sighting sighting =new Sighting();

        //get information to create sighting object
        String strTitle = title.getText().toString().trim();
        int id = sightingIDCount;
        User owner = new User(user);
        Species species = new Species(animalIDCount, strTitle);
        db.addAnimal(species);
        Calendar calendar = Calendar.getInstance();
        String description = desc.getText().toString().trim();
        //UPDATE LATER
        String fileName = "";
        ArrayList<String> tagList = new ArrayList<>();
        String tagStr=tags.getText().toString().trim();
        String[] tagArr = tagStr.split(" ");
        for(int i=0; i<tagArr.length;i++){
            tagList.add(tagArr[i]);
        }

        sighting.setTitle(strTitle);
        sighting.setID(id);
        sighting.setOwner(owner);
        sighting.setAnimal(species);
        sighting.setLocation(location);
        sighting.setTimestamp(calendar);
        sighting.setDescription(description);
        sighting.setImageFileName(fileName);
        sighting.setFlagCount(0);
        sighting.setTags(tagList);


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
