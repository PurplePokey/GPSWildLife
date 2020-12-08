package com.example.wildlifegps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

public class deleteSighting extends AppCompatActivity implements View.OnClickListener {

    private final deleteSighting activity = deleteSighting.this;

    private Button b;
    private DBHandler dbh;
    private Sighting sighting;
    private List<Sighting> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighting_view);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        b = (Button)findViewById(R.id.delete_sighting);
    }

    private void initListeners(){
        b.setOnClickListener(this);
    }

    private void initObjects(){
        dbh =new DBHandler(activity);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==(R.id.delete_sighting)){
            dbh.deleteSighting(sighting);

            Toast.makeText(activity, "Sighting Successfully Deleted", Toast.LENGTH_LONG).show();

            list = dbh.filterByTime();

            Intent intentDelete = new Intent(getApplicationContext(), ListView.class);
            intentDelete.putExtra("deletedSightingList", (Serializable) list);
            startActivity(intentDelete);

        }
    }
    
}

