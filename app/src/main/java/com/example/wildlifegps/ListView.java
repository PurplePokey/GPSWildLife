package com.example.wildlifegps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ListView extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ListView.this;
    private Intent i;
    private String user;

    private Button addSighting;

    private DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        i =getIntent();
        user= i.getExtras().getString("user");

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();

        //check location permissions are allowed
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            }
        }
    }

    private void initViews(){
        addSighting=(Button) findViewById(R.id.add_sighting);
    }

    private void initListeners(){
        addSighting.setOnClickListener(this);
    }

    private void initObjects(){
        db=new DBHandler(activity);
    }

    @Override
    public void onClick(View view){

        if(view.getId()==(R.id.add_sighting)){
            Intent intentSightingSelector = new Intent(getApplicationContext(), SightingSelector.class);
            intentSightingSelector.putExtra("user", user);
            startActivity(intentSightingSelector);

        }

    }

}
