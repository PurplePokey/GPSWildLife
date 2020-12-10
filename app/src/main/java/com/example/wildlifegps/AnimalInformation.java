package com.example.wildlifegps;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalInformation extends AppCompatActivity {

    private TextView commName;
    private TextView specName;
    private TextView diet;
    private TextView appearance;
    private TextView stat;
    private LineChart mChart;
    private DBHandler db;
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
        mChart = findViewById(R.id.chart);
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
        ArrayList<Entry> values = new ArrayList<>();
        XAxis xAxis = mChart.getXAxis();
        YAxis yAxis = mChart.getAxisLeft();
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(false);
        DBHandler db=new DBHandler(this);
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, -7);
        int count = 0;
        for(int i = 1; i < 8; i++){
            count = db.getDailySightingCount(animal.getAnimalID(), date);
            values.add(new Entry(i, count));
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
        /*values.add(new Entry(1, 50));
        values.add(new Entry(2, 100));
        values.add(new Entry(3, 75));*/

        LineDataSet set = new LineDataSet(values, animal.getCommonName() + " sightings per day"
            +" this past week");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
    }
}
