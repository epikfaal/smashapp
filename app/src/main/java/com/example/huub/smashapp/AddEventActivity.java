package com.example.huub.smashapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Date;
import java.util.Calendar;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class AddEventActivity extends AppCompatActivity {
    EditText name, longtitude, latitude;
    DatePicker date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Button button = (Button) findViewById(R.id.submitbutton);
        name = (EditText) findViewById(R.id.nameTextField);
        date = (DatePicker) findViewById(R.id.datePicker);
        longtitude = (EditText) findViewById(R.id.longTextField);
        latitude = (EditText) findViewById(R.id.latTextField);
        Button locationButton = (Button) findViewById(R.id.locationbutton);
        date.updateDate(date.getYear(), date.getMonth(), date.getDayOfMonth());

        // getIntent().getStringExtra("function") could be null, this gets around exception calling .equals on a null object
        final String functionstring = getIntent().getStringExtra("function") == null ?  "" : getIntent().getStringExtra("function");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                data.putExtra("name", name.getText().toString());
                data.putExtra("eventyear", date.getYear());
                data.putExtra("eventmonth", date.getMonth() + 1);
                data.putExtra("eventday", date.getDayOfMonth());
                data.putExtra("longtitude", Float.parseFloat(longtitude.getText().toString()));
                data.putExtra("latitude", Float.parseFloat(latitude.getText().toString()));
                if(functionstring.equals("edit")) data.putExtra("id", getIntent().getLongExtra("id", -1));
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("function", "picklocation");
                //TODO create static final variables for these requestcodes
                startActivityForResult(intent, 1234);
            }
        });



        if(functionstring.equals("edit")){
            Intent intent = getIntent();

            DataSource dataSource = new DataSource(this);
            dataSource.open();

            System.out.println(intent.getLongExtra("id", -1));
            Cursor event = dataSource.getEvent(intent.getLongExtra("id", -1));

            // Move to the first row returned, since this is NOT standard behaviour
            event.moveToFirst();
            name.setText(event.getString(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTNAME)));
            longtitude.setText(event.getString(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLONGTITUDE)));
            latitude.setText(event.getString(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTLATITUDE)));

            String[] dateString = event.getString(event.getColumnIndex(Event.EventDBEntry.COLUMN_NAME_EVENTDATE)).split("-");
            date.updateDate(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]) - 1, Integer.parseInt(dateString[0]));
            button.setText("Save Changes");
            dataSource.close();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == Activity.RESULT_OK){
            longtitude.setText(data.getDoubleExtra("long", 0) + "");
            latitude.setText(data.getDoubleExtra("lat", 0) + "");
        }
    }
}
