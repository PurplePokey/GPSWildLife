package com.example.wildlifegps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ListView extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ListView.this;

    private EditText username;
    private EditText password;
    private EditText password2;


    private Button create_btn;
    private Button login_btn;

    private DBHandler db;


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

    }

    private void initListeners(){

    }

    private void initObjects(){
        db=new DBHandler(activity);
    }

    @Override
    public void onClick(View view){


    }

}
