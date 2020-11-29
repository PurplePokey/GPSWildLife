package com.example.wildlifegps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class deleteSighting extends Activity {

    private final deleteSighting activity = deleteSighting.this;

    Button b;
    DBHandler dbh;
    Sighting sighting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighting_view);

        b = (Button)findViewById(R.id.delete_sighting);

        try
        {
            dbh = new DBHandler(activity);

        }
        catch(SQLiteException e)
        {
            System.out.print("ERROR");
        }

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dbh.deleteSighting(sighting);

                Toast.makeText(activity, "Sighting Successfully Deleted", Toast.LENGTH_LONG).show();
                Intent intentDelete = new Intent(getApplicationContext(), ListView.class);
                startActivity(intentDelete);
            }
        });
    }

}

