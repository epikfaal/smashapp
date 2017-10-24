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

    private List<Event> eventList;
    private Cursor dbCursor;
    private EventListClickListener clickListener;
    private Location currentLocation;

    public EventListAdapter(Cursor cursor, EventListClickListener listener)
    {
        dbCursor = cursor;
        clickListener = listener;
        currentLocation = new Location("");
    }

    public interface EventListClickListener{
        void onEventClick(long index);
    }
    public void swapCursor(Cursor cursor){
        if(dbCursor != null) dbCursor.close();
        dbCursor = cursor;

        notifyDataSetChanged();
    }

    public void setLocation(Location newLocation){
        currentLocation = newLocation;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.event_row_item, null);


        System.out.println("test1");

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, final int position) {
        if(!dbCursor.moveToPosition(position)){
            return;
        }
        String name, distance, unit, date;

        Location eventLocation = new Location("");
        eventLocation.setLongitude(dbCursor.getFloat(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE)));
        eventLocation.setLatitude(dbCursor.getFloat(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE)));

        name = dbCursor.getString(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTNAME));
        distance = (eventLocation.distanceTo(currentLocation)/1000) + "";
        unit = "km";
        date = dbCursor.getString(dbCursor.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTDATE));

        holder.name.setText(name);
        holder.distance.setText(distance);
        holder.unit.setText(unit);
        holder.date.setText(date);

        // get the ID of the current row, this is NOT equal to the position integer

        final Long index = dbCursor.getLong(dbCursor.getColumnIndex(Event.EventDBEntry._ID));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onEventClick(index);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dbCursor.getCount();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView name, distance, unit, date;
        public View mView;

        EventViewHolder(View v){
            super(v);
            mView = v;
            name = (TextView) v.findViewById(R.id.eventName);
            distance = (TextView) v.findViewById(R.id.distance);
            unit = (TextView) v.findViewById(R.id.unit);
            date = (TextView) v.findViewById(R.id.eventDate);
        }
    }
}
