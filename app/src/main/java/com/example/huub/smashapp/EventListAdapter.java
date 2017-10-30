package com.example.huub.smashapp;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Huub on 10/10/2017.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private Cursor dbCursor;
    private EventListClickListener clickListener;
    private Location currentLocation;


    /**
     * Creates the EventListAdapter
     * @param cursor Cursor containing all events
     * @param listener Listener containing functions handeling clicks on the edit button and the location
     */
    public EventListAdapter(Cursor cursor, EventListClickListener listener)
    {
        dbCursor = cursor;
        clickListener = listener;
        currentLocation = new Location("");
    }

    /**
     * interface for the ClickListeners
     */
    public interface EventListClickListener{
        void onEventLocationClick(long index);
        void onEditEventClick(long index);
    }

    /**
     * Change cursor containing the events we want displayed
     * @param cursor Cursor containing events to get displayed
     */
    public void swapCursor(Cursor cursor){
        if(dbCursor != null) dbCursor.close();
        dbCursor = cursor;

        notifyDataSetChanged();
    }

    /**
     * Set the current location of the user (callback for the location service)
     * @param newLocation The new location of the user
     */
    public void setLocation(Location newLocation){
        currentLocation = newLocation;
    }

    @Override
    /**
     * Set the Viewholder to be our custom Viewholder with the layout of an "event_row_item"
     */
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.event_row_item, null);
        return new EventViewHolder(view);
    }

    @Override
    /**
     * Set all the information of the event to the right place in the viewholder
     */
    public void onBindViewHolder(EventViewHolder holder, final int position) {
        if(!dbCursor.moveToPosition(position)){
            return;
        }
        String name, distance, unit, date;

        Location eventLocation = new Location("");
        eventLocation.setLongitude(dbCursor.getFloat(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE)));
        eventLocation.setLatitude(dbCursor.getFloat(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE)));

        name = dbCursor.getString(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTNAME));
        distance = String.format("%.1f", eventLocation.distanceTo(currentLocation)/1000);
        // originally wanted to change the unit to metres when very close to a location but due to time constraints, went with allways Kilometres
        unit = "km";
        date = dbCursor.getString(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTDATE));

        holder.name.setText(name);
        holder.distance.setText(distance);
        holder.unit.setText(unit);
        holder.date.setText(date);

        // get the ID of the current row, this is NOT equal to the position integer

        final Long index = dbCursor.getLong(dbCursor.getColumnIndex(Event.EventDBEntry._ID));

        View.OnClickListener showOnmapClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onEventLocationClick(index);
            }
        };
        holder.distance.setOnClickListener(showOnmapClickListener);
        holder.unit.setOnClickListener(showOnmapClickListener);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onEditEventClick(index);
            }
        });

    }

    @Override
    /**
     * Returns the amount of items in the Adapter
     */
    public int getItemCount() {
        return dbCursor.getCount();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView name, distance, unit, date, edit;
        public View mView;

        EventViewHolder(View v){
            super(v);
            mView = v;
            name = (TextView) v.findViewById(R.id.eventName);
            distance = (TextView) v.findViewById(R.id.distance);
            unit = (TextView) v.findViewById(R.id.unit);
            date = (TextView) v.findViewById(R.id.eventDate);
            edit = (TextView) v.findViewById(R.id.editEvent);
        }
    }
}
