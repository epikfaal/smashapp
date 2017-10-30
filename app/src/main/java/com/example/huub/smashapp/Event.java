package com.example.huub.smashapp;

import android.provider.BaseColumns;

/**
 * Created by Huub on 10/10/2017.
 */

public class Event {
    /*
    This class was originally going to be used as objects that would be created from the query of the global database.
    However due to time constraints I decided to change the app so it at least complies to the rubric.
    therefore this class is empty and it has a subclass that holds information for the database
     */
    public static class EventDBEntry implements BaseColumns{
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENTNAME = "eventname";
        public static final String COLUMN_NAME_EVENTLONGTITUDE = "eventlong";
        public static final String COLUMN_NAME_EVENTLATITUDE = "eventlat";
        public static final String COLUMN_NAME_EVENTDATE = "eventdate";

        public static final String[] ALL_COLUMNS = {_ID, COLUMN_NAME_EVENTNAME, COLUMN_NAME_EVENTDATE, COLUMN_NAME_EVENTLONGTITUDE, COLUMN_NAME_EVENTLATITUDE};
    }
}
