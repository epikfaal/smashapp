package com.example.huub.smashapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;

import java.sql.Date;

/**
 * Created by Huub on 22/10/2017.
 */

public class DataSource {
    private Context context;
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;

    DataSource(Context ct){
        context = ct;
        dbHelper = new DataBaseHelper(context);
    }

    /**
     * creates a writebale Database
     */
    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the current open database object
     */
    public void close(){
        dbHelper.close();
    }

    /**
     * Adds an event row to the table
     *
     * @param name Name of the event
     * @param date Date of the event in "DD-MM-YYYY" format
     * @param longtitude The longtitude of the location of the event
     * @param latitude The latitude of the location of the event
     */
    public void createEvent(String name, String date, float longtitude, float latitude){
        ContentValues values = new ContentValues();
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTNAME, name);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTDATE, date);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE, longtitude);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE, latitude);
        db.insert(Event.EventDBEntry.TABLE_NAME, null, values);
    }

    /**
     * Gets all the events currently in the Database
     * @return Returns a cursor containing all information of all events
     */
    public Cursor getEvents(){
        return db.query(Event.EventDBEntry.TABLE_NAME, Event.EventDBEntry.ALL_COLUMNS, null, null, null, null, null);
    }

    /**
     * Gets a single event
     * @param id The id of the event we want returned
     * @return Returns a cursor containing a single row with all information about the event
     */
    public Cursor getEvent(long id){
        System.out.println(Event.EventDBEntry._ID + " = " + id);
        return db.query(Event.EventDBEntry.TABLE_NAME, Event.EventDBEntry.ALL_COLUMNS, Event.EventDBEntry._ID + " = " + id, null, null, null, null);

    }

    /**
     * Deletes an event
     * @param id the id of the event we want deleted
     */
    public void deleteEvent(long id){
        db.delete(Event.EventDBEntry.TABLE_NAME, Event.EventDBEntry._ID + "=?", new String[]{"" + id});
    }

    /**
     * Updates an existing event in the database
     * @param id The id of the event we want to update
     * @param newName The new name of the event
     * @param newDate The new date of the event
     * @param newLongtitude The new longtitude of the location of the event
     * @param newLatitude The new latitude of the location of the event
     */
    public void updateEvent(long id, String newName, String newDate, float newLongtitude, float newLatitude){
        ContentValues values = new ContentValues();
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTNAME, newName);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTDATE, newDate);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE, newLongtitude);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE, newLatitude);
        db.update(Event.EventDBEntry.TABLE_NAME, values, Event.EventDBEntry._ID + " = " + id, null);
    }

    /**
     * Gets the ids of all events currently in the Database
     * @return Returns an array of type long containing all event ids
     */
    public long[] getAllEventIds(){
        Cursor events = db.query(Event.EventDBEntry.TABLE_NAME, new String[]{Event.EventDBEntry._ID}, null, null, null, null, null);
        long[] IDs = new long[events.getCount()];
        int i = 0;
        while(events.moveToNext()){
            IDs[i] = events.getLong(events.getColumnIndex(Event.EventDBEntry._ID));
            i++;
        }
        return IDs;
    }


}
