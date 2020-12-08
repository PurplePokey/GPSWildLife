package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class filterByTime extends AppCompatActivity {

    private final AppCompatActivity activity = filterByTime.this;

    private DBHandler db;
    private ArrayList<Sighting> arrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        getSupportActionBar().hide();

        db=new DBHandler(activity);
        arrayList = db.filterByTime();

        Intent intentFilterByTime = new Intent(getApplicationContext(), Sighting.class);
        intentFilterByTime.putExtra("filteredList", (Serializable) arrayList);
        startActivity(intentFilterByTime);
    }

}
