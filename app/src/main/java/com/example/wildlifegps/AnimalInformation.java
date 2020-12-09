package com.example.wildlifegps;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalInformation extends AppCompatActivity {

    private TextView commName;
    private TextView specName;
    private TextView diet;
    private TextView appearance;
    private TextView stat;
//    private LineChart mChart;
    Animal animal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_info);

        getSupportActionBar().hide();

        initObjects();
        initViews();
        initListeners();
        display();
    }
    private void initObjects(){
        animal = (Animal) getIntent().getSerializableExtra("Animal");
    }
    private void initViews(){
        commName = findViewById(R.id.common_name_info_text);
        specName = findViewById(R.id.scientific_name_info_text);
        diet = findViewById(R.id.diet_info);
        stat = findViewById(R.id.status_info);
        appearance = findViewById(R.id.appearance_info);
//        mChart = findViewById(R.id.chart);
    }
    private void initListeners(){

    }
    private void display(){
        if(animal != null && animal instanceof Species){
            Species species = (Species) animal;
            commName.setText(species.getCommonName());
            specName.setText(species.getScienceName());
            diet.setText(species.getDiet());
            stat.setText(species.getConserveStatus());
            appearance.setText(species.getAppearance());
        }
        else{
            Toast.makeText(getApplicationContext(), "Error: unexpected input", Toast.LENGTH_LONG).show();
        }
        createChart();
    }
    private void createChart(){
//        ArrayList<Entry> values = new ArrayList<>();
    }
}
