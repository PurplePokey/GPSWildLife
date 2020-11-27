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

public class Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_test_screen);

        // Finds the warning button from the xml layout file
        Button warningNotificationButton = findViewById(R.id.buttonWarn);

        // Waits for you to click the button
        warningNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                warnNotification();
            }
        });

        // Finds the found pet button from the xml layout file
        Button foundNotificationButton = findViewById(R.id.buttonWarn);

        // Waits for you to click the button
        foundNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starts the function below
                foundNotification();
            }
        });
    }

    // Creates and displays a notification
    private void warnNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Watch out, You're about to be eaten!")
                .setContentText("There is a(n) Animal[Object] Near your location.");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, Notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    // Creates and displays a notification
    private void foundNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Your pet has been found!")
                .setContentText("Your pet has been marked on your map.");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, Notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
