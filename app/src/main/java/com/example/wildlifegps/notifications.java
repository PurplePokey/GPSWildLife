package com.example.wildlifegps;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.lang.Object;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

public class notifications extends AppCompatActivity {
    boolean Subscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

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
