package com.example.wildlifegps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class login extends AppCompatActivity implements View.OnClickListener{

    private final AppCompatActivity activity = login.this;

    private EditText username;
    private EditText password;

    private Button login_btn;
    private Button register_btn;

    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();


    }

    private void initViews(){
        username=(EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);

        login_btn = (Button) findViewById(R.id.login);
        register_btn= (Button) findViewById(R.id.register);
    }

    private void initListeners(){
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    private void initObjects(){
        db=new DBHandler(activity);
    }

    @Override
    public void onClick(View view){

        //if the login button is pressed
        if(view.getId()==(R.id.login)){
            //validate fields are filled
            if(checkInput(username)&& checkInput(password)){
                //if filled check it matches a data base entry, go to list view
                if(db.searchUser(username.getText().toString().trim(), password.getText().toString().trim())){
                    Intent intentListView = new Intent(getApplicationContext(), ListView.class);
                    startActivity(intentListView);
                }
                else{
                    username.setText(null);
                    password.setText(null);
                }

            }

            //else give warning that username or password was invalid
            else{
                username.setText(null);
                password.setText(null);
            }
        }
        //if the register button is pressed
        if(view.getId()==(R.id.register)){
            //go to register_view
            Intent intentRegister = new Intent(getApplicationContext(), Register.class);
            startActivity(intentRegister);
        }

    }

    private boolean checkInput(EditText text){
        String value = text.getText().toString().trim();
        if(value.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
}
