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
import android.widget.TextView;

import java.lang.Object;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

public class notifications extends AppCompatActivity {
    boolean Subscribed;
    private LocationManager locationManager;
    private LocationListener locationListener;
    TextView t = findViewById(R.id.textLocation);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                t.append("\n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

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
                    LocationUpdate();

                }
            }
        });



        //Found pet listener
        foundNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                foundNotification();
            }
        });
    }

    void LocationUpdate(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        //noinspection MissingPermission
        //locationManager.requestLocationUpdates("gps", 5000, 0, LocationListener);

    }

    // Creates and displays a warning notification
    private void warnNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Watch out, You're about to be eaten!")
                .setContentText("There is a(n) Animal[Object] Near your location.");

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
                .setContentTitle("Your pet has been found!")
                .setContentText("Your pet has been marked on your map.");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
