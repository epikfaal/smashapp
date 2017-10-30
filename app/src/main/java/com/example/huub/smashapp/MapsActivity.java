package com.example.huub.smashapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    // These are some constants that create a more pleasant map viewing experience (set to target the netherlands)
    private final float PREFFERED_ZOOM_LEVEL_EVENT = 12.0f;
    private final float PREFFERED_ZOOM_LEVEL_OVERVIEW = 7.0f;
    private final LatLng POSITION_NETHERLANDS = new LatLng(52.13373, 5.395707);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = getIntent();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DataSource source = new DataSource(this);
        source.open();

        // If we want to show a single event
        // We get the event, create a marker and move the camera to the event position
        if(intent.getStringExtra("function").equals("showevent")){
            Cursor event = source.getEvent(intent.getLongExtra("id", -1));

            for(int i = 0; i < event.getColumnNames().length; i++)
                System.out.println(event.getColumnNames()[i]);

            // Move to the first object, since cursors apperently do NOT start there
            event.moveToFirst();
            double longtitude = event.getDouble(event.getColumnIndexOrThrow(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE));
            double latitude = event.getDouble(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE));
            // Add a marker at the location of the event and move the camera
            LatLng eventloc = new LatLng(latitude, longtitude);
            mMap.addMarker(new MarkerOptions().position(eventloc).title("Event"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventloc));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(PREFFERED_ZOOM_LEVEL_EVENT));
        }

        // If we want to show all events
        // We get all events, create markers for each one and move the camera to the netherlands
        if(intent.getStringExtra("function").equals("showAllEvents")){
            // The reason we do it with an array containing all event IDs and don't use DataSource.getAllEvents()
            // is because I originally planned to be able to show each event in a country, howerver due to time constraints this never made it
            long[] eventIDs = intent.getLongArrayExtra("id");
            for(int i = 0; i < eventIDs.length; i++){
                Cursor event = source.getEvent(eventIDs[i]);

                // Move to the first and only row since this is NOT normal behaviour
                event.moveToFirst();
                String eventName = event.getString(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTNAME));
                double latitude = event.getDouble(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE));
                double longtitude = event.getDouble(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE));

                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).title(eventName));

            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(POSITION_NETHERLANDS));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(PREFFERED_ZOOM_LEVEL_OVERVIEW));
        }

        // If we want the user to pick a location
        // We create a clicklistener that returns to the last activity with the location
        if(intent.getStringExtra("function").equals("picklocation")) {

            mMap.moveCamera(CameraUpdateFactory.newLatLng(POSITION_NETHERLANDS));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(PREFFERED_ZOOM_LEVEL_OVERVIEW));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent();
                    intent.putExtra("long", latLng.longitude);
                    intent.putExtra("lat", latLng.latitude);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }
        source.close();
    }
}
