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
import android.widget.Button;
import android.widget.ToggleButton;

import java.sql.Date;
import java.util.ArrayList;

public class OverViewActivity extends AppCompatActivity implements EventListAdapter.EventListClickListener{
    public final static int DELETE_EVENT_REQUEST = 1234567;
    public final static int ADD_EVENT_ACTIVITY = 1234;
    public final static int EDIT_EVENT_ACTIVITY = 1235;
    RecyclerView rv;

    DataSource data;
    EventListAdapter rvAdapter;
    Button viewOnMapButton;
    FloatingActionButton fab;
    LocationManager locationManager;
    Cursor dbCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        // Check for coarse location permission
        while ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    1234 );
        }

        rv = (RecyclerView) findViewById(R.id.recyclerview);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        data = new DataSource(this);
        data.open();

        viewOnMapButton = (Button) findViewById(R.id.mapsToggle);

        // Create a vertical LinearLayoutManager for the RecyclerView, since we want our events to be displayed vertically
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        // Create a locationlistener that updates the current location for the recyclerview
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

        // Create a clicklistener for the floating action button to create a new event
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddEventActivity.class);
                startActivityForResult(intent, ADD_EVENT_ACTIVITY);
            }
        });

        // Create a clicklistener for the view all events on map button to show all events on the map
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("function", "showAllEvents");
                intent.putExtra("id", data.getAllEventIds());
                startActivity(intent);
            }
        });

        // let the locationlistener listen to the lcoation service
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);

        updateUI();
    }

    @Override
    /**
     * Create, delete or update events based on which action was requested after returning from the "AddEventActivity"
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == Activity.RESULT_OK){
            String eventdate = data.getIntExtra("eventday", 1)+"-"+data.getIntExtra("eventmonth",1)+"-"+data.getIntExtra("eventyear", 2020);
            this.data.createEvent(data.getStringExtra("name"), eventdate, data.getFloatExtra("longtitude", 0), data.getFloatExtra("latitude", 0));
            updateUI();
        }

        if(requestCode == EDIT_EVENT_ACTIVITY && resultCode == Activity.RESULT_OK){
            String eventdate = data.getIntExtra("eventday", 1)+"-"+data.getIntExtra("eventmonth",1)+"-"+data.getIntExtra("eventyear", 2020);
            //System.out.println("TESTID= " + data.getLongExtra("id", -1));
            this.data.updateEvent(data.getLongExtra("id", -1), data.getStringExtra("name"), eventdate, data.getFloatExtra("longtitude", 0), data.getFloatExtra("latitude", 0));
            updateUI();
        }

        if(requestCode == EDIT_EVENT_ACTIVITY && resultCode == DELETE_EVENT_REQUEST){
            this.data.deleteEvent(data.getLongExtra("id", -1));
            System.out.println("delete request send");
            updateUI();
        }
    }

    /**
     * update the UI
     * This function sends a new cursor containing all the events to the EventAdapter
     */
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
    /**
     * Show the event on the map if clicked on its location
     * @param index The id of the event clicked
     */
    public void onEventLocationClick(long index) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("function", "showevent");
        intent.putExtra("id", index);
        startActivity(intent);

    }

    @Override
    /**
     * Edit the event if clicked on the edit button
     * @param index The id of the event clicked
     */
    public void onEditEventClick(long index){
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("function", "edit");
        intent.putExtra("id", index);
        startActivityForResult(intent, EDIT_EVENT_ACTIVITY);
    }
}
