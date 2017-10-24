package com.example.huub.smashapp;

import android.provider.BaseColumns;

/**
 * Created by Huub on 10/10/2017.
 */

public class Event {
    private String unit, name;
    //temp:
    private double distance;

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    public Event(){
        unit = "KM";
        name = "Sample Event";
        distance = 8.4;
    }
    public Event (String unit, String name, double distance){
        this.unit = unit;
        this.name = name;
        this.distance = distance;
    }
    public static class EventDBEntry implements BaseColumns{
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_EVENTNAME = "eventname";
        public static final String COLUMN_NAME_EVENTLONGTITUDE = "eventlong";
        public static final String COLUMN_NAME_EVENTLATITUDE = "eventlat";
        public static final String COLUMN_NAME_EVENTDATE = "eventdate";

        public static final String[] ALL_COLUMNS = {_ID, COLUMN_NAME_EVENTNAME, COLUMN_NAME_EVENTDATE, COLUMN_NAME_EVENTLONGTITUDE, COLUMN_NAME_EVENTLATITUDE};
    }
}
