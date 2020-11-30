package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ListView extends AppCompatActivity{

    private final AppCompatActivity activity = ListView.this;

    private EditText username;
    private EditText password;
    private EditText password2;


    private Button create_btn;
    private Button login_btn;

    private ImageView image;

    private DBHandler db;

    private Animal testAnimal;
    private Sighting testSighting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        image = findViewById(R.id.animal_image);


    }

    private void initListeners(){
        image.setOnClickListener(click);
    }

    private void initObjects(){
        db=new DBHandler(activity);
        testAnimal = new Species(1, "Hummingbird", "Archilochus colubris", "LC", "nectar", "cute");
        testSighting = new Sighting();
        testSighting.setAnimal(testAnimal);
        testSighting.setOwner(new User("fake", "extra fake"));
        testSighting.setDescription("Look it's a bird, no it's a plane, no it's really a bird");
    }

    //@Override
    private View.OnClickListener click = new View.OnClickListener() {
        public void onClick(View view) {

            Intent i = new Intent(ListView.this, ViewSighting.class);
            i.putExtra("Sighting", testSighting);
            startActivity(i);
        }
    };

}
