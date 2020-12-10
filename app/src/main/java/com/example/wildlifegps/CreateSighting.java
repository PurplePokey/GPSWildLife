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

import com.google.android.gms.maps.GoogleMap;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.wildlifegps.DBHandler.animalIDCount;
import static com.example.wildlifegps.DBHandler.sightingIDCount;

public class CreateSighting extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = CreateSighting.this;

    private LocationManager locationManager;
    private MyLocationListener mylistener;

    private ImageView photo;

    private EditText title;
    private EditText desc;
    private EditText tags;

    private Switch identify;

    private Intent i;
    private String user;

    private double latitude;
    private double longitude;
    private Location location;

    private Uri selectedImage;
    private Button upload;
    private Button cancel;
    private Button create;

    Bitmap bitmap = null;
    byte img[];

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
        photo=(ImageView) findViewById(R.id.sighting_photo);

        title=(EditText) findViewById(R.id.sighting_title_create);
        desc = (EditText) findViewById(R.id.sighting_desc_create);
        tags = (EditText) findViewById(R.id.sighting_tags_create);

        identify=(Switch) findViewById(R.id.requestIdenSwitch);

        upload=(Button) findViewById(R.id.upload_image);
        cancel = (Button) findViewById(R.id.cancel_create_sighting);
        create= (Button) findViewById(R.id.create_sighting);
    }

    private void initListeners(){
        cancel.setOnClickListener(this);
        create.setOnClickListener(this);
        upload.setOnClickListener(this);

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
        //upload image
        if(view.getId()==(R.id.upload_image)){
            upload();
        }

        //if create sighting button is pressed
        if(view.getId()==(R.id.create_sighting)){
            //validate title field is filled if request switch is off
            if(checkInput(title)){
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
    private void upload(){
        Intent pickPhoto= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        sighting.setImageFileName(selectedImage);
        sighting.setFlagCount(0);
        sighting.setTags(tagList);

        db.addSighting(sighting);

        //agg tags to the database
        for(int i=0; i<tagArr.length;i++){
            db.addTag(sighting, tagArr[i]);
        }



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
