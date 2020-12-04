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

import org.junit.Test;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class deleteSighting extends AppCompatActivity implements View.OnClickListener {

    private final deleteSighting activity = deleteSighting.this;

    private Button b;
    private DBHandler dbh;
    private Sighting sighting;

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
        b.setOnClickListener((View.OnClickListener) this);
    }

    private void initObjects(){
        dbh =new DBHandler(activity);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==(R.id.login)){
            dbh.deleteSighting(sighting);

            Toast.makeText(activity, "Sighting Successfully Deleted", Toast.LENGTH_LONG).show();

            Intent intentDelete = new Intent(getApplicationContext(), ListView.class);
            intentDelete.putExtra("Sighting", (Serializable) sighting);
            startActivity(intentDelete);

        }
    }
    
}

