package com.example.wildlifegps;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.Calendar;

public class ListView extends AppCompatActivity{

    private final AppCompatActivity activity = ListView.this;

    private ImageView image;
    private LinearLayout cards;
    private Drawable roundBackground;

    private DBHandler db;

    private Animal testAnimal;
    private Sighting testSighting;

    private ArrayList<Sighting> results = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
        display();
    }

    private void initViews(){
       // image = findViewById(R.id.animal_image);
        cards = findViewById(R.id.cardView);
        roundBackground = getDrawable(R.drawable.rounded_textbox);
    }

    private void initListeners(){
       // image.setOnClickListener(click);
    }

    private void initObjects(){
        db=new DBHandler(activity);
        Calendar[] testCals = new Calendar[3];
        Animal[] testAnims = new Animal[3];
        User[] testUsers = new User[3];
        String[] descripts = new String[3];
        for(int i = 0; i < testCals.length; i++){
            testCals[i] = Calendar.getInstance();
        }
        testCals[0].set(2020, 11, 7, 2, 4);
        testCals[1].set(2020, 11, 5, 14, 30);
        testCals[2].set(2019, 3, 5, 7, 28);
        testAnims[0] = new Species(1, "Hummingbird", "Archilochus colubris", "LC", "nectar", "cute");
        testAnims[1] = new Species(2, "Fox", "Vulpes vulpes", "LC", "rodents", "red");
        testAnims[2] = new Species(3, "Cottontail", "Sylvilagus floridanus", "LC", "your garden", "bunny");
        testUsers[0] = new User("FakeUser", "extra fake");
        testUsers[1] = new User("Greg", "boop");
        testUsers[2] = new User("Bob", "bobbo");
        descripts[0] = "This bird flies so fast it scares me";
        descripts[1] = "Found this fox in my backyard!";
        descripts[2] = "BUN BUN";

        for(int i = 0; i < descripts.length; i++){
            Sighting tmp = new Sighting();
            tmp.setOwner(testUsers[i]);
            tmp.setTimestamp(testCals[i]);
            tmp.setAnimal(testAnims[i]);
            tmp.setDescription(descripts[i]);
            results.add(tmp);
        }
    }

    private void display(){
        Calendar time = Calendar.getInstance();
        for(int i = 0; i < results.size(); i++){
            LinearLayout bigBox = new LinearLayout(this);
            LayoutParams bigParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            bigParams.setMargins(50, 50, 50, 50);
            bigBox.setLayoutParams(bigParams);
            bigBox.setPadding(20, 20, 20, 20);

            LinearLayout lilBox = new LinearLayout(this);
            lilBox.setOrientation(LinearLayout.VERTICAL);
            bigBox.setBackground(roundBackground);
            bigBox.addView(lilBox);
            TextView speciesTest = new TextView(this);
            speciesTest.setText(results.get(i).getAnimal().getCommonName());
            lilBox.addView(speciesTest);

            //TextView distanceText = new TextView(this);
            TextView recencyText = new TextView(this);
            String tmp;
            Calendar c = results.get(i).getTimestamp();
            tmp = timeElapsed(c, time);
            recencyText.setText(tmp);
            lilBox.addView(recencyText);
            cards.addView(bigBox);
        }
    }

    private String timeElapsed (Calendar olderTime, Calendar curTime){
        long secondsSinceCreation = (curTime.getTimeInMillis() - olderTime.getTimeInMillis()) / 1000;
        if(secondsSinceCreation < 61){
            return secondsSinceCreation + " seconds ago";
        }
        else if(secondsSinceCreation < 3601){
            return (secondsSinceCreation / 60) + " minute(s) ago";
        }
        else if(secondsSinceCreation < 86401){
            return (secondsSinceCreation / 3600) + " hour(s) ago";
        }
        else if(secondsSinceCreation < (86400 * 365 + 1)){
            return (secondsSinceCreation / 86400) + " day(s) ago";
        }
        else{
            return (secondsSinceCreation / (86400 * 365)) + " year(s) ago";
        }
    }

    private View.OnClickListener click = new View.OnClickListener() {
        public void onClick(View view) {

            Intent i = new Intent(ListView.this, ViewSighting.class);
            i.putExtra("Sighting", results.get(0));
            startActivity(i);
        }
    };

}
