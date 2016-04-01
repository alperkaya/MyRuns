package com.example.alperkaya.myruns;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

public class StartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private final static String[] ENTRIES = new String[]{"Date", "Time", "Duration", "Distance",
            "Calories", "Heart Rate", "Comment"};
    Calendar mDateAndTime = Calendar.getInstance();
    private ListView entryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        entryListView = (ListView) findViewById(R.id.entryListView);


        ArrayAdapter<String> entryAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1,
                        android.R.id.text1, ENTRIES);

        entryListView.setAdapter(entryAdapter);

        entryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {

                    case 0:
                        new DatePickerDialog(
                                StartActivity.this,
                                StartActivity.this,
                                mDateAndTime.get(Calendar.YEAR),
                                mDateAndTime.get(Calendar.MONTH),
                                mDateAndTime.get(Calendar.DAY_OF_MONTH)
                        ).show();
                        break;

                    case 1:
                        new TimePickerDialog(
                                StartActivity.this,
                                StartActivity.this,
                                mDateAndTime.get(Calendar.HOUR),
                                mDateAndTime.get(Calendar.MINUTE),
                                true).show();
                        break;

                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDateAndTime.set(Calendar.YEAR, year);
        mDateAndTime.set(Calendar.MONTH, monthOfYear);
        mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


}
