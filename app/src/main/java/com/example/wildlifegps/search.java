package com.example.wildlifegps;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class search extends AppCompatActivity implements Serializable {

    private final AppCompatActivity activity = search.this;


    private DBHandler dbh;
    private SearchView searchView;
    private ArrayList<Sighting> list;
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        searchView = (SearchView) findViewById(R.id.search_View);
        spin = (Spinner)findViewById(R.id.search_list_dropdown);
        dbh = new DBHandler(activity);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(search.this, query,Toast.LENGTH_LONG).show();
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

                        } else {
                            // Display appropriate message when Geocoder services are not available
                            Toast.makeText(activity, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        // handle exception
                    }
                }
                else if(spin.getSelectedItemPosition() == 1){
                    list = dbh.searchByTag(query);
                    Toast.makeText(search.this, query,Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(activity, "No Match found",Toast.LENGTH_LONG).show();

                }
                Intent intentSearch = new Intent(getApplicationContext(), Sighting.class);
                startActivity(intentSearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

        });
    }

}
