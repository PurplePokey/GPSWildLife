package com.example.wildlifegps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

    private LinearLayout cards;
    private Drawable roundBackground;

    private DBHandler db;
    private LocationManager locManager;
    private Location currentLoc;

    private ArrayList<Sighting> results = new ArrayList<>();
    private Intent i;
    private String user;

    private Button addSighting;
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
        addSighting=(Button) findViewById(R.id.add_sighting);
        cards = findViewById(R.id.cardView);
        roundBackground = getDrawable(R.drawable.rounded_textbox);
    }

    private void initListeners(){
        addSighting.setOnClickListener(clickCreateSighting);
        display();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    private void initObjects(){
        db=new DBHandler(activity);

        //What follows is a bunch of placeholder data, replace it to integrate with db and search
        Calendar[] testCals = new Calendar[3];
        Animal[] testAnims = new Animal[3];
        User[] testUsers = new User[3];
        String[] descripts = new String[3];
        String[] imgs = new String[3];
        Location[] locs = new Location[3];

        for(int i = 0; i < testCals.length; i++){
            testCals[i] = Calendar.getInstance();
        }
        testCals[0].set(2020, 11, 7, 2, 4);
        testCals[1].set(2020, 11, 5, 14, 30);
        testCals[2].set(2019, 3, 5, 7, 28);
        testAnims[0] = new Species(1, "Hummingbird", "Archilochus colubris", "LC", "nectar", "cute");
        testAnims[1] = new Species(2, "Fox", "Vulpes vulpes", "LC", "rodents", "red");
        testAnims[2] = new Species(3, "Cottontail", "Sylvilagus floridanus", "LC", "your garden", "bunny");
        testUsers[0] = new User("FakeUser", "extra fake");
        testUsers[1] = new User("Greg", "boop");
        testUsers[2] = new User("Bob", "bobbo");
        descripts[0] = "This bird flies so fast it scares me";
        descripts[1] = "Found this fox in my backyard!";
        descripts[2] = "BUN BUN";
        imgs[0] = "hummingbird";
        imgs[1] = "fox";
        imgs[2] = "cottontail";
        locs[0] = new Location("");
        locs[0].setLatitude(42.9);
        locs[0].setLongitude(-87.7);

        locs[1] = new Location("");
        locs[1].setLatitude(40);
        locs[1].setLongitude(-80);

        locs[2] = new Location("");
        locs[2].setLatitude(35.5);
        locs[2].setLongitude(-85.3);

        for(int i = 0; i < descripts.length; i++){
            Sighting tmp = new Sighting();
            tmp.setOwner(testUsers[i]);
            tmp.setTimestamp(testCals[i]);
            tmp.setAnimal(testAnims[i]);
            tmp.setDescription(descripts[i]);
           // tmp.setImageFileName(imgs[i]);
            tmp.setLocation(locs[i]);
            results.add(tmp);
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

    private View.OnClickListener clickCreateSighting = new View.OnClickListener(){
        public void onClick(View view){
            Intent intentCreateSighting = new Intent(getApplicationContext(), CreateSighting.class);
            intentCreateSighting.putExtra("user", user);
            startActivity(intentCreateSighting);
        }
    };


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
