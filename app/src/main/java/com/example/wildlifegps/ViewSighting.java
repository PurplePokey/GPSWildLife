package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewSighting extends AppCompatActivity {

    private final AppCompatActivity activity = ViewSighting.this;

    private Sighting sighting = null;
    private EditText commonNameBox;
    private TextView scienceNameBox;
    private TextView userBox;
    private TextView descriptBox;
    private Button learnButton;
    private Button delete;

    private ArrayList<Sighting> list;

    private DBHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighting_view);

        getSupportActionBar().hide();

        initObjects();
        initViews();
        initListeners();
        display();
    }

    //Get sighting from intent
    private void initObjects(){
        sighting = (Sighting) getIntent().getSerializableExtra("Sighting");
        dbh = new DBHandler(activity);
    }

    //initialize view objects
    private void initViews(){
        //commonNameBox = (EditText) findViewById(R.id.common_species_name);
        scienceNameBox = (TextView) findViewById(R.id.scientific_name);
        userBox = findViewById(R.id.poster_username);
        descriptBox = findViewById(R.id.sighting_desc);
        learnButton = findViewById(R.id.learn_more_btn);
        delete = findViewById(R.id.delete_sighting);
    }

    //create listeners
    private void initListeners(){
        if(sighting != null && sighting.getAnimal() instanceof Species){
            learnButton.setOnClickListener(learnMore);
        }
        else{
            learnButton.setVisibility(View.INVISIBLE);
        }

        delete.setOnClickListener(deleteSighting);
    }

    //set view objects to show sighting info rather than placeholders
    private void display(){
        if(sighting != null){
            //Common display
            String commonName = sighting.getAnimal().getCommonName();
            commonNameBox.setText(commonName);
            userBox.setText(sighting.getOwner().getUsername());
            descriptBox.setText(sighting.getDescription());
            if(sighting.getAnimal() instanceof Species){
                //Species specific display
                scienceNameBox.setText(((Species) sighting.getAnimal()).getScienceName());

            }
            else{
                //Pet specific display
            }
        }
    }
    private View.OnClickListener learnMore = new View.OnClickListener() {
        public void onClick(View view) {
            Intent i = new Intent(ViewSighting.this, AnimalInformation.class);
            i.putExtra("Sighting", (Parcelable) sighting);
            startActivity(i);
        }
    };

    private View.OnClickListener deleteSighting = new View.OnClickListener() {
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
    };
}
