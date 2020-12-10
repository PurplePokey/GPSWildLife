package com.example.wildlifegps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.w3c.dom.Text;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class ListView extends AppCompatActivity{

    private final AppCompatActivity activity = ListView.this;
    private Intent i;
    private String user;

    private Button addSighting;
    private Button submit;
    private ArrayList<Sighting> list = new ArrayList<>();
    private Spinner spin;
    private EditText tv;
    private String query;

    private LinearLayout cards;
    private Drawable roundBackground;

    private DBHandler db;
    private LocationManager locManager;
    private Location currentLoc;

    private ArrayList<Sighting> results = new ArrayList<>();
    private ListLocationListener mylistener;

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
        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            }
        }
        else {
            //Toast.makeText(getApplicationContext(), "Location enabled", Toast.LENGTH_SHORT).show();
            mylistener = new ListLocationListener();
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1, mylistener);
            if(locManager!=null){
                currentLoc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(currentLoc!=null){
                //   Toast.makeText(getApplicationContext(), "Location: " + currentLoc.toString(), Toast.LENGTH_SHORT).show();
            }
            else{
                //    Toast.makeText(getApplicationContext(), "No location found", Toast.LENGTH_SHORT).show();
            }

        }

        display();
    }

    private void initViews(){
        cards = findViewById(R.id.cardView);
        roundBackground = getDrawable(R.drawable.rounded_textbox);
        addSighting=(Button) findViewById(R.id.add_sighting);
        submit = (Button) findViewById(R.id.submit);

        tv = findViewById(R.id.searchview);
        spin = findViewById(R.id.search_list_dropdown);
    }

    private void initListeners(){
        addSighting.setOnClickListener(clickCreateSighting);
        submit.setOnClickListener(clickSubmit);
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    private void initObjects(){
        db=new DBHandler(activity);
        results = (ArrayList<Sighting>) getIntent().getSerializableExtra("Sighting");

        if(results == null){
            results = db.filterByTime();
        }

        if(results == null || results.size() == 0){
            Toast.makeText(getApplicationContext(), "No sightings found", Toast.LENGTH_SHORT).show();
        }

    }


    private void display(){
        Calendar time = Calendar.getInstance();
        cards.getChildAt(0).setVisibility(View.GONE);
        for(int i = 0; i < results.size(); i++){
            //Create main container
            LinearLayout bigBox = new LinearLayout(this);
            LayoutParams bigParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            bigParams.setMargins(50, 50, 50, 50);
            bigBox.setLayoutParams(bigParams);
            bigBox.setOrientation(LinearLayout.HORIZONTAL);
            bigBox.setBackground(roundBackground);
            bigBox.setPadding(20, 20, 20, 20);
            bigBox.setId(i);
            bigBox.setOnClickListener(click);
            //Add image
            ImageView imgBox = new ImageView(this);
            imgBox.setImageResource(R.drawable.fox);
            LayoutParams imgParams = new LayoutParams(250, 250);
            imgBox.setLayoutParams(imgParams);
            bigBox.addView(imgBox);

            //Add text boxes
            LinearLayout lilBox = new LinearLayout(this);
            lilBox.setOrientation(LinearLayout.VERTICAL);
            bigBox.addView(lilBox);
            TextView speciesTest = new TextView(this);
            speciesTest.setText(results.get(i).getAnimal().getCommonName());
            lilBox.addView(speciesTest);

            //add distance
            TextView distanceText = new TextView(this);

            if(currentLoc!=null && results.get(i).getLocation()!=null){
                float distance = currentLoc.distanceTo(results.get(i).getLocation());
                String tmp = distanceString(distance);
                distanceText.setText(tmp);
            }
            else{
                distanceText.setText("Distance: unknown");
            }
            lilBox.addView(distanceText);

            TextView recencyText = new TextView(this);
            String tmp;
            Calendar c = results.get(i).getTimestamp();
            tmp = timeElapsed(c, time);
            recencyText.setText(tmp);
            lilBox.addView(recencyText);

            //Add button to view sighting
            //I figured out how to add the listener to the LinearLayout box, so this is
            //no longer necessary
           /* ImageButton arrow = new ImageButton(this);
            arrow.setImageResource(R.mipmap.forward_white_blue_round);
            LayoutParams imgParams2 = new LayoutParams(200, 200);
            imgParams2.setMargins(100, 0, 0, 0);
            arrow.setLayoutParams(imgParams2);
            arrow.setId(i);
            arrow.setOnClickListener(click);
            bigBox.addView(arrow);*/

            cards.addView(bigBox);
        }
    }

    private void recalculateDistances(){
        if(currentLoc!=null){
            for (int i = 0; i < results.size(); i++){
                if(results.get(i).getLocation()!=null){
                    LinearLayout bigBox = findViewById(i);
                    if(bigBox != null){
                        LinearLayout lilBox = (LinearLayout) bigBox.getChildAt(1);
                        if(lilBox != null){
                            TextView distanceText = (TextView) lilBox.getChildAt(1);
                            if(distanceText != null){
                                float distance = currentLoc.distanceTo(results.get(i).getLocation());
                                String tmp = distanceString(distance);
                                distanceText.setText(tmp);
                            }
                        }
                    }
                }
            }
        }
    }

    private String distanceString(float distance){
        String result = "Distance: ";
        if(distance < 1000.0){
            result += String.format("%.2f meters", distance);
        }
        else{
            result += String.format("%.2f km", distance / 1000.0);
        }
        return result;
    }

    private String timeElapsed (Calendar olderTime, Calendar curTime){
        long secondsSinceCreation = (curTime.getTimeInMillis() - olderTime.getTimeInMillis()) / 1000;
        if(secondsSinceCreation < 61){
            return secondsSinceCreation + " seconds ago";
        }
        else if(secondsSinceCreation < 3601){
            return (secondsSinceCreation / 60) + " minute(s) ago";
        }
        else if(secondsSinceCreation < 86401){
            return (secondsSinceCreation / 3600) + " hour(s) ago";
        }
        else if(secondsSinceCreation < (86400 * 365 + 1)){
            return (secondsSinceCreation / 86400) + " day(s) ago";
        }
        else{
            return (secondsSinceCreation / (86400 * 365)) + " year(s) ago";
        }
    }

    //clicking the plus button
    private View.OnClickListener clickCreateSighting = new View.OnClickListener(){
        public void onClick(View view){
            Intent intentSightingSelect = new Intent(getApplicationContext(), SightingSelector.class);
            intentSightingSelect.putExtra("user", user);
            startActivity(intentSightingSelect);
        }
    };

    private View.OnClickListener clickSubmit = new View.OnClickListener(){
        public void onClick(View view){
            if(view.getId()==(R.id.submit)) {

                if (!(tv.getText().toString().trim().equals(""))) {
                    query = tv.getText().toString().trim();

                    if (spin.getSelectedItemPosition() == 0) {
                        final Geocoder geocoder = new Geocoder(activity);
                        if (query.matches("[0-9]+") && query.length() == 5) {

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
                        } else if (!(query.matches("[0-9]+") && query.length() == 5)) {
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
    };

    //for clicking sightings
    private View.OnClickListener click = new View.OnClickListener() {
        public void onClick(View view) {
            int id = view.getId();
            //Toast.makeText(getApplicationContext(), "Clicked on sighting #" + id, Toast.LENGTH_SHORT).show();

            if(id >= 0 && id < results.size()){
                Intent i = new Intent(ListView.this, ViewSighting.class);
                i.putExtra("Sighting", results.get(id));
                i.putExtra("user", user);
                startActivity(i);
            }
        }
    };

    private class ListLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location){
            currentLoc = location;
            // Toast.makeText(getApplicationContext(), "Location changed: " + currentLoc.toString(), Toast.LENGTH_SHORT).show();
            recalculateDistances();
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
