package com.example.huub.smashapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ToggleButton;

import java.sql.Date;
import java.util.ArrayList;

public class OverViewActivity extends AppCompatActivity implements EventListAdapter.EventListClickListener{
    RecyclerView rv;

    DataSource data;
    EventListAdapter rvAdapter;
    ArrayList<Event> eventList;
    FloatingActionButton fab;
    LocationManager locationManager;

    Cursor dbCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    1234 );
        }



        rv = (RecyclerView) findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        data = new DataSource(this);
        data.open();

        //PURELY DEBUGGING LOGGING CODE TODO: remove
        System.out.println("DATABASE DEBUG:");
        data.debug();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                rvAdapter.setLocation(location);
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // TODO: create final variables for activityresults
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddEventActivity.class);
                startActivityForResult(intent, 1234);
            }
        });

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);

        updateUI();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == Activity.RESULT_OK){
            String eventdate = data.getIntExtra("eventday", 1)+"-"+data.getIntExtra("eventMonth",1)+"-"+data.getIntExtra("eventYear", 2020);
            this.data.createEvent(data.getStringExtra("name"), eventdate, data.getFloatExtra("longtitude", 0), data.getFloatExtra("latitude", 0));
            updateUI();
        }
    }

    private void updateUI(){
        dbCursor = data.getEvents();
        if(rvAdapter == null) {
            rvAdapter = new EventListAdapter(dbCursor, this);
            rv.setAdapter(rvAdapter);
        } else {
            rvAdapter.swapCursor(dbCursor);
        }
    }

    @Override
    public void onEventLocationClick(long index) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("function", "showevent");
        intent.putExtra("id", index);
        startActivity(intent);

    }

    @Override
    public void onEditEventClick(long index){
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("function", "edit");
        intent.putExtra("id", index);
        startActivityForResult(intent, 1235);
    }


}
