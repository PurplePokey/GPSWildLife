package com.example.wildlifegps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class deleteSighting extends Activity {

    Button b;
    DBHandler dbh;
    Sighting sighting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighting_view);
        b=(Button)findViewById(R.id.delete_sighting);
        final Context context = getApplicationContext();

        try
        {
            dbh = new DBHandler(context);
        }
        catch(SQLiteException e)
        {
            System.out.print("ERROR");
        }
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dbh.deleteSighting(sighting);

                Intent i = new Intent(context, Sighting.class);
                startActivity(i);
            }
        });
    }

}
