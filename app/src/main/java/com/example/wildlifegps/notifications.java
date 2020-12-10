package com.example.wildlifegps;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class notifications extends AppCompatActivity {
    boolean Subscribed;

    private LocationManager locationManager;
    private MyLocationListener mylistener;
    private double latitude;
    private double longitude;
    private Location location;
    Sighting sighting_variable = new Sighting();
    //suppress these locations:
    private Location listOfLocations[] ={};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        updateLocation();
        compareLocation();


        // Finds the subscribe button from the xml layout file
        final Button SubNotificationButton = findViewById(R.id.subButton);
        SubNotificationButton.setVisibility(View.INVISIBLE); // Set button to invisible
        Subscribed = true; // Set Subscribed variable to True.

        // Finds the unsubscribe button from the xml layout file
        final Button UnsubNotificationButton = findViewById(R.id.unsubButton);


        // Finds the warning button from the xml layout file
        Button warningNotificationButton = findViewById(R.id.buttonWarn);
        // Finds the found pet button from the xml layout file
        Button foundNotificationButton = findViewById(R.id.buttonFound);
        Button createFoundNotificationButton = findViewById(R.id.create_pet_lost);

        //Subscribing
        SubNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                UnsubNotificationButton.setVisibility(View.VISIBLE); // Set button to visible
                SubNotificationButton.setVisibility(View.INVISIBLE); // Set button to invisible
                Subscribed = true;


            }
        });

        //Unsubscribing
        UnsubNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                SubNotificationButton.setVisibility(View.VISIBLE); // Set button to visible
                UnsubNotificationButton.setVisibility(View.INVISIBLE); // Set button to invisible
                Subscribed = false;

            }
        });

        //Warning listener

        warningNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                if(Subscribed == true) {
                    warnNotification();
                    //LocationUpdate();

                }
            }
        });



        //Found pet listener
        foundNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                if(Subscribed == true) {
                    foundNotification();
                }
            }
        });

    /*  //uncomment after merge:
        createFoundNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                if(Subscribed == true) {
                    foundNotification();
                }
            }
        });

     */
    }

    private void updateLocation(){
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
                compareLocation();
            }
        }
    }


    private void compareLocation() {
        //updateLocation();
        //check sighting
        if(location !=null && sighting_variable.getLocation() !=null) {
            if (location == sighting_variable.getLocation()) {
                //check if user is subscribed for notifications
                if (Subscribed == true) {
                    //suppress notification at selected locations
                    for (int i = 0; i > listOfLocations.length; i++) {
                        if (location != listOfLocations[i]) {
                            warnNotification();
                        }
                    }
                }
            }
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

    // Creates and displays a warning notification
    private void warnNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Look around there was a sighting in your area!")
                .setContentText("There is a(n) "+sighting_variable.getAnimal()+" near your location.");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    // Creates and displays a found pet notification
    private void foundNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Your lost pet has been found")
                .setContentText("Your pet will be marked on your map when found");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


}
