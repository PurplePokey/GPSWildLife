package com.example.wildlifegps;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = search.this;

    private DBHandler dbh;
    private SearchView searchView;
    private ArrayList<Sighting> list = new ArrayList<>();
    private Spinner spin;
    private Button button;
    private EditText tv;
    private String query;

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
        //searchView = (SearchView) findViewById(R.id.search_View);
        spin = (Spinner)findViewById(R.id.search_list_dropdown);

        tv = (EditText)findViewById(R.id.searchview);
        button = (Button)findViewById(R.id.submit);
    }

    private void initListeners(){
        //spin.setOnItemClickListener(spin.getOnItemClickListener());
        //searchView.setOnQueryTextListener(this);
        button.setOnClickListener(this);
    }
    private void initObjects(){
        dbh=new DBHandler(activity);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==(R.id.submit)) {

            if (!(tv.getText().toString().trim().equals(""))) {
                query = tv.getText().toString().trim();
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();

                if (spin.getSelectedItemPosition() == 0) {
                    final Geocoder geocoder = new Geocoder(activity);
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(query, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            // Use the address as needed
                            double lat = address.getLatitude();
                            double lon = address.getLongitude();
                            Location loc = new Location("");
                            loc.setLatitude(lat);
                            loc.setLongitude(lon);

                            list = dbh.searchByLocation(loc);

                            Intent intentSearch = new Intent(activity, ListView.class);
                            intentSearch.putExtra("Sighting", list);
                            startActivity(intentSearch);

                        } else {
                            // Display appropriate message when Geocoder services are not available
                            Toast.makeText(activity, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        // handle exception
                    }
                } else if (spin.getSelectedItemPosition() == 1) {

                    list = dbh.searchByTag(query);
                    Toast.makeText(search.this, query, Toast.LENGTH_LONG).show();

                    Intent intentSearch = new Intent(activity, ListView.class);
                    intentSearch.putExtra("Sighting", list);
                    startActivity(intentSearch);

                } else {
                    Toast.makeText(activity, "No Match found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter Text", Toast.LENGTH_LONG).show();
            }

        }
    }

    /*@Override
    public boolean onQueryTextSubmit(String query1) {

        String query = (String)searchView.getQuery();
        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();

        if(!(searchView.getQuery().equals(""))){
            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
            Intent intentSearch = new Intent(activity, ListView.class);
            startActivity(intentSearch);
        }
        else{
            Toast.makeText(getApplicationContext(), "Enter Text", Toast.LENGTH_LONG).show();
        }

        if(spin.getSelectedItemPosition() == 0){

            final Geocoder geocoder = new Geocoder(activity);
            try {
                List<Address> addresses = geocoder.getFromLocationName(query, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    // Use the address as needed
                    double lat = address.getLatitude();
                    double lon = address.getLongitude();
                    Location loc = new Location("");
                    loc.setLatitude(lat);
                    loc.setLongitude(lon);

                    list = dbh.searchByLocation(loc);

                    Intent intentSearch = new Intent(activity, ListView.class);
                    intentSearch.putExtra("Sighting", list);
                    startActivity(intentSearch);

                } else {
                    // Display appropriate message when Geocoder services are not available
                    Toast.makeText(activity, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                }
                return true;
            } catch (IOException e) {
                // handle exception
            }
        }
        else if(spin.getSelectedItemPosition() == 1){
            list = dbh.searchByTag(query);
            Toast.makeText(search.this, query,Toast.LENGTH_LONG).show();
            Intent intentSearch = new Intent(activity, ListView.class);
            intentSearch.putExtra("Sighting", list);
            startActivity(intentSearch);
            return true;
        }
        else{
            Toast.makeText(activity, "No Match found",Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }*/

}
