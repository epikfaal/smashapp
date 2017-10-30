package com.example.huub.smashapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Huub on 22/10/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "SmashDB.db";
    public static final int DBVERSION = 1;

    // Create a DataBaseHelper object
    public DataBaseHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    /**
     * gets called if the database just got created
     * creates a table based on the data in Event.EventDBEntry
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Event.EventDBEntry.TABLE_NAME +"(" + Event.EventDBEntry._ID + " INTEGER PRIMARY KEY," +
                Event.EventDBEntry.COLUMN_NAME_EVENTNAME + " TEXT," +
                Event.EventDBEntry.COLUMN_NAME_EVENTDATE + " TEXT," +
                Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE + " REAL," +
                Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE + " REAL )");
    }

    @Override
    /**
     * gets called if there is a new version of the database, currently there has never been a new version
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
