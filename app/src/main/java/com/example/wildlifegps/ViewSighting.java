package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class ViewSighting extends AppCompatActivity {

    private final AppCompatActivity activity = ViewSighting.this;

    private Sighting sighting = null;
    private String user = "";
    private TextView commonNameBox;
    private TextView scienceNameBox;
    private TextView userBox;
    private TextView descriptBox;
    private Button learnButton;
    private Button delete;
    private Button updateButton;
    private Button backButton;
    private Button stillHere;
    private Button moved;
    private TextView identify;
    private ImageView img;
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
        user = getIntent().getStringExtra("user");
        dbh = new DBHandler(activity);
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
        delete= findViewById(R.id.delete_sighting);
        updateButton = findViewById(R.id.updateSightingBtn);
        backButton = findViewById(R.id.sighting_back_button);
        stillHere = (Button) findViewById(R.id.animal_there_btn);
        moved= (Button) findViewById(R.id.animal_gone_btn);

    }

    //create listeners
    private void initListeners(){
        backButton.setOnClickListener(goBack);
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

        if(!user.equals(sighting.getOwner().getUsername())){
            delete.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
        }
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
            else{
                commonNameBox.setText("Unknown");
                scienceNameBox.setVisibility(View.GONE);
            }
        }
    }
    private View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    private View.OnClickListener learnMore = new View.OnClickListener() {
        public void onClick(View view) {
            Intent i = new Intent(ViewSighting.this, AnimalInformation.class);
            i.putExtra("Animal", sighting.getAnimal());
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
    private View.OnClickListener animalStillHere = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==(R.id.animal_there_btn)){

            }
        }
    };
    private View.OnClickListener animalMoved = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==(R.id.animal_gone_btn)){

            }
        }
    };
}
