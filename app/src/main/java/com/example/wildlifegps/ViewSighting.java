package com.example.wildlifegps;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewSighting extends AppCompatActivity {

    private Sighting sighting = null;
    private EditText commonNameBox;
    private TextView scienceNameBox;

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
    }

    //initialize view objects
    private void initViews(){
        commonNameBox = (EditText) findViewById(R.id.common_species_name);
        scienceNameBox = (TextView) findViewById(R.id.scientific_name);
    }

    //create listeners
    private void initListeners(){

    }

    //set view objects to show sighting info rather than placeholders
    private void display(){
        if(sighting != null){
            //Common display
            String commonName = sighting.getAnimal().getCommonName();
            commonNameBox.setText(commonName);
            if(sighting.getAnimal() instanceof Species){
                //Species specific display

            }
            else{
                //Pet specific display
            }

        }
    }
}
