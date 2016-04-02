package com.example.alperkaya.myruns;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
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

public class ManualEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private final static String[] ENTRIES = new String[]{"Date", "Time", "Duration", "Distance",
            "Calories", "Heart Rate", "Comment"};
    Calendar mDateAndTime = Calendar.getInstance();
    private ListView entryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

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
                                ManualEntryActivity.this,
                                ManualEntryActivity.this,
                                mDateAndTime.get(Calendar.YEAR),
                                mDateAndTime.get(Calendar.MONTH),
                                mDateAndTime.get(Calendar.DAY_OF_MONTH)
                        ).show();
                        break;

                    case 1:
                        new TimePickerDialog(
                                ManualEntryActivity.this,
                                ManualEntryActivity.this,
                                mDateAndTime.get(Calendar.HOUR),
                                mDateAndTime.get(Calendar.MINUTE),
                                true
                        ).show();
                        break;

                    case 2:
                        DialogFragment dialogFragment = MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_ID_DURATION);
                        dialogFragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_duration));
                        break;
                    case 3:
                        DialogFragment distFragment = MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_ID_DISTANCE);
                        distFragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_distance));
                        break;
                    case 4:
                        DialogFragment calFragment = MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_ID_CALORIES);
                        calFragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_calories));
                        break;
                    case 5:
                        DialogFragment heartFragment = MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_ID_HEART_RATE);
                        heartFragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_heart_rate));
                        break;
                    case 6:
                        DialogFragment commFragment = MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_ID_COMMENT);
                        commFragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_comment));
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
        mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mDateAndTime.set(Calendar.MINUTE, minute);
    }

    // Handle when user clicks "Save" button
    public void onSaveButtonClicked(View view){

    }

    // Handle when user clicks "Cancel" button
    public void onCancelButtonClicked(View view) {
        finish();
    }
}
