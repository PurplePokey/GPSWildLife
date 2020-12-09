package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.wildlifegps.R.id.flag_sighting;

public class ViewSighting extends AppCompatActivity {

    private Sighting sighting = null;
    private TextView commonNameBox;
    private TextView scienceNameBox;
    private TextView userBox;
    private TextView descriptBox;
    private Button learnButton;
    private TextView identify;
    private ImageView img;
    private Button flag_sighting;

    private DBHandler db;



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
        commonNameBox = findViewById(R.id.common_species_name);
        scienceNameBox = findViewById(R.id.scientific_name);
        userBox = findViewById(R.id.poster_username);
        descriptBox = findViewById(R.id.sighting_desc);
        learnButton = findViewById(R.id.learn_more_btn);
        identify = findViewById(R.id.identify_txt);
        img = findViewById(R.id.sighting_photo);
        flag_sighting = findViewById(R.id.flag_sighting);
    }

    //create listeners
    private void initListeners(){
        if(sighting != null && sighting.getAnimal() instanceof Species){
            learnButton.setOnClickListener(learnMore);
        }
        else{
            learnButton.setVisibility(View.INVISIBLE);
        }

        flag_sighting.setOnClickListener((View.OnClickListener) this);
    }

    //set view objects to show sighting info rather than placeholders
    private void display(){
        if(sighting != null){
            //Common display
            String commonName = sighting.getAnimal().getCommonName();
            commonNameBox.setText(commonName);
            userBox.setText(sighting.getOwner().getUsername());
            descriptBox.setText(sighting.getDescription());
            //show placeholder image
            img.setImageResource(R.drawable.fox);
            if(sighting.getAnimal() != null){
                identify.setVisibility(View.GONE);
                if(sighting.getAnimal() instanceof Species){
                    //Species specific display
                    scienceNameBox.setText(((Species) sighting.getAnimal()).getScienceName());
                }
                else{
                    //Pet specific display
                    scienceNameBox.setText(((Pet) sighting.getAnimal()).getPetName());
                }
            }

        }
    }
    private final View.OnClickListener learnMore = new View.OnClickListener() {
        public void onClick(View view) {
            Intent i = new Intent(ViewSighting.this, AnimalInformation.class);
            i.putExtra("Animal", sighting.getAnimal());
            startActivity(i);

            //Flag Count starts here
            if(view.getId()==(R.id.flag_sighting))
            {
                int count = sighting.getFlagCount();
                count++;
                sighting.setFlagCount(count);
                db.updateFlagCount(sighting);
            }
        }
    };



}

