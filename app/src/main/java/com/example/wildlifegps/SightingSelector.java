package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SightingSelector extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = SightingSelector.this;

    private Button wildAnimal;
    private Button foundPet;
    private Button lostPet;

    private Intent i;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighting_selector);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();

    }

    public void initViews(){
        wildAnimal = (Button) findViewById(R.id.animal_sighting_btn);
        foundPet= (Button) findViewById(R.id.lost_pet_btn);
        lostPet= (Button) findViewById(R.id.found_pet_btn);

        i =getIntent();
        user= i.getExtras().getString("user");
    }

    private void initListeners(){
        wildAnimal.setOnClickListener(this);
        foundPet.setOnClickListener(this);
        lostPet.setOnClickListener(this);
    }

    private void initObjects(){

    }

    @Override
    public void onClick(View view){

        //if the create wild animal sighting button is pressed
        if(view.getId()==(R.id.animal_sighting_btn)){
            Intent intentSightingCreate = new Intent(getApplicationContext(), CreateSighting.class);
            intentSightingCreate.putExtra("user", user);
            startActivity(intentSightingCreate);
            finish();
        }
        //if the found pet button is pressed
        if(view.getId()==(R.id.lost_pet_btn)){
            Intent intentAddLostPet = new Intent(getApplicationContext(), LostPet.class);
            intentAddLostPet.putExtra("user", user);
            startActivity(intentAddLostPet);
            finish();
        }
        //if the lost pet button is pressed
        if(view.getId()==(R.id.found_pet_btn)){

        }

    }
}
