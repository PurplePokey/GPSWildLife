package com.example.wildlifegps;

import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity{

    private final search activity = search.this;

    DBHandler dbh;
    SearchView searchView;
    ListView listView;
    ArrayList<Sighting> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        searchView = (SearchView) findViewById(R.id.searchView);

        try
        {
            dbh = new DBHandler(activity);

        }
        catch(SQLiteException e)
        {
            System.out.print("ERROR");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.matches("[0-9]+") && query.length() == 5){

                }
                else if(query.matches("[a-zA-Z]+")){
                    list = dbh.searchByTag(query);
                }
                else{
                    Toast.makeText(search.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
