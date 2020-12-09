package com.example.wildlifegps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ListView.this;
    private Intent i;
    private String user;

    private Button addSighting;
    private Button submit;
    private ArrayList<Sighting> list = new ArrayList<>();
    private Spinner spin;
    private EditText tv;
    private String query;

    private DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        i =getIntent();
        user= i.getExtras().getString("user");

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();

        //check location permissions are allowed
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            }
        }
    }

    private void initViews(){
        addSighting=(Button) findViewById(R.id.add_sighting);
        submit = (Button) findViewById(R.id.submit);

        tv = findViewById(R.id.searchview);
        spin = findViewById(R.id.search_list_dropdown);
    }

    private void initListeners(){
        addSighting.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void initObjects(){
        db=new DBHandler(activity);
    }

    @Override
    public void onClick(View view){

        if(view.getId()==(R.id.add_sighting)){
            Intent intentCreateSighting = new Intent(getApplicationContext(), CreateSighting.class);
            intentCreateSighting.putExtra("user", user);
            startActivity(intentCreateSighting);

        }

        if(view.getId()==(R.id.submit)) {

            if (!(tv.getText().toString().trim().equals(""))) {
                query = tv.getText().toString().trim();

                if (spin.getSelectedItemPosition() == 0) {
                    final Geocoder geocoder = new Geocoder(activity);
                    if(query.matches("[0-9]+") && query.length() == 5) {

                        try {
                            List<Address> addresses = geocoder.getFromLocationName(query, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                // Use the address as needed
                                double lat = address.getLatitude();
                                double lon = address.getLongitude();
                                Location loc = new Location("");
                                loc.setLatitude(lat);
                                loc.setLongitude(lon);

                                list = db.searchByLocation(loc);

                                Intent intentSearch = new Intent(activity, ListView.class);
                                intentSearch.putExtra("Sighting", list);
                                startActivity(intentSearch);

                            } else {
                                // Display appropriate message when Geocoder services are not available
                                Toast.makeText(activity, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                            }

                        } catch (IOException e) {
                            // handle exception
                        }
                    }
                    else if(!(query.matches("[0-9]+") && query.length() == 5)){
                        Toast.makeText(activity, "Enter a valid zip code", Toast.LENGTH_LONG).show();
                    }
                } else if (spin.getSelectedItemPosition() == 1) {
                    
                    list = db.searchByTag(query);
                    Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();

                    Intent intentSearch = new Intent(activity, ListView.class);
                    intentSearch.putExtra("Sighting", list);
                    startActivity(intentSearch);

                } else {
                    Toast.makeText(activity, "No Match found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter Text", Toast.LENGTH_LONG).show();
            }

        }

    }

}