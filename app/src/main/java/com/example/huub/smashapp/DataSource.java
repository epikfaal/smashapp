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

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void createEvent(String name, String date, float longtitude, float latitude){
        ContentValues values = new ContentValues();
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTNAME, name);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTDATE, date);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE, longtitude);
        values.put(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE, latitude);
        db.insert(Event.EventDBEntry.TABLE_NAME, null, values);
    }

    public Cursor getEvents(){
        return db.query(Event.EventDBEntry.TABLE_NAME, Event.EventDBEntry.ALL_COLUMNS, null, null, null, null, null);
    }

    public Cursor getEvent(long id){
        System.out.println(Event.EventDBEntry._ID + " = " + id);
        return db.query(Event.EventDBEntry.TABLE_NAME, Event.EventDBEntry.ALL_COLUMNS, Event.EventDBEntry._ID + " = " + id, null, null, null, null);

    }

    //function for debugging database purposes
    public void debug(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + Event.EventDBEntry.TABLE_NAME, null);

        String[] colums = cursor.getColumnNames();
        for (int i = 0; i < colums.length; i++){
            System.out.println(colums[i]);
            System.out.println(cursor.getColumnIndex(colums[i]));

           // System.out.println(cursor.getLong(cursor.getColumnIndex(colums[i])));

        }
        System.out.println(Event.EventDBEntry._ID);
        System.out.println(cursor.getColumnIndex(Event.EventDBEntry._ID));
        System.out.println(Event.EventDBEntry._ID.equals(colums[0]));

    }


}
