package com.example.wildlifegps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.wildlifegps.DBHandler.animalIDCount;
import static com.example.wildlifegps.DBHandler.sightingIDCount;

public class FoundPet extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = FoundPet.this;

    private LocationManager locationManager;
    private MyLocationListener mylistener;

    private ImageView photo;

    private EditText type;
    private EditText name;
    private EditText desc;

    private Intent i;
    private String user;

    private double latitude;
    private double longitude;
    private Location location;

    private Button upload;
    private Button cancel;
    private Button create;

    private Uri selectedImage;

    Bitmap bitmap = null;
    byte img[];

    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_pet);

        getSupportActionBar().hide();
        i =getIntent();
        user= i.getExtras().getString("user");

        initViews();
        initListeners();
        initObjects();

    }
    private void initViews(){
        photo=(ImageView) findViewById(R.id.found_pet_pic);
        type=(EditText) findViewById(R.id.type_pet_found);
        name = (EditText) findViewById(R.id.name_pet_found);
        desc = (EditText) findViewById(R.id.desc_pet_found);
        upload=(Button) findViewById(R.id.upload_found);
        cancel = (Button) findViewById(R.id.cancel_pet_found);
        create= (Button) findViewById(R.id.create_pet_found);
    }

    private void initListeners(){
        cancel.setOnClickListener(this);
        create.setOnClickListener(this);
        upload.setOnClickListener(this);
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
        if(view.getId()==(R.id.upload_found)){
            upload();
        }

        //if create sighting button is pressed
        if(view.getId()==(R.id.create_pet_found)){
            //validate title field is filled if request switch is off
            if(checkInput(type)){
                //validate desc field is filled
                if(checkInput(name)){

                    addFoundPet();
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
        if(view.getId()==(R.id.cancel_pet_found)) {
            finish();
        }

    }

    private void upload() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode== Activity.RESULT_OK && data !=null)
        {
            selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                img = bos.toByteArray();
                photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addFoundPet(){
        Sighting sighting =new Sighting();

        //get information to create sighting object
        String strTitle = type.getText().toString().trim();
        String petname = name.getText().toString().trim();
        int id = sightingIDCount;
        User owner = new User(user);
        Pet pet = new Pet (animalIDCount, strTitle,db.petIDCount, petname);
        pet.setLostFound(1);
        db.addPet(pet);
        Calendar calendar = Calendar.getInstance();
        String description = desc.getText().toString().trim();

        sighting.setTitle(strTitle);
        sighting.setID(id);
        sighting.setOwner(owner);
        sighting.setAnimal(pet);
        sighting.setLocation(location);
        sighting.setTimestamp(calendar);
        sighting.setDescription(description);
        sighting.setImageFileName(selectedImage);

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
