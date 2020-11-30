package com.example.wildlifegps;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class search extends AppCompatActivity implements Serializable {

    private final search activity = search.this;

    DBHandler dbh;
    SearchView searchView;
    ArrayList<Sighting> list;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search_View);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        spin = (Spinner)findViewById(R.id.search_list_dropdown);
        dbh = new DBHandler(activity);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(spin.getSelectedItemPosition() == 0){
                    Address add = new Address(Locale.US);
                    add.setPostalCode(query);
                    double lat = add.getLatitude();
                    double lon = add.getLongitude();
                    Location loc = new Location("");
                    loc.setLatitude(lat);
                    loc.setLongitude(lon);
                    list = dbh.searchByLocation(loc);
                }
                else if(spin.getSelectedItemPosition() == 1){
                    list = dbh.searchByTag(query);
                }
                else{
                    Toast.makeText(search.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                Intent intentSearch = new Intent(getApplicationContext(), Sighting.class);
                startActivity(intentSearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }

}
