package com.example.admin.testingv1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class addEvent extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = addEvent.class.getSimpleName();
    private TextView theDate;
    private Button backToEventsToday;
    private FirebaseAuth firebaseAuth;
    private TextView startDate;
    private TextView endDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private String date;
    private TextView startTime;
    private TextView endTime;
    private TimePickerDialog.OnTimeSetListener startTimeSetListener;
    private TimePickerDialog.OnTimeSetListener endTimeSetListener;
    private DatabaseReference eventDB;
    private Button addEventBtn;
    private EditText eventName;
    private EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        theDate = (TextView) findViewById(R.id.theDate);
        backToEventsToday = (Button) findViewById(R.id.backToEventsToday);
        addEventBtn = (Button) findViewById(R.id.addEvent);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent incomingIntent = getIntent();
        date = incomingIntent.getStringExtra("date");

        remarks = (EditText) findViewById(R.id.remarks);
        eventName = (EditText) findViewById(R.id.eventName);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);
        startTime.setText("08:00");
        endTime.setText("09.00");
        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeSet: hh:mm " + hourOfDay + ":" + minute);

                String time = hourOfDay + ":" + minute;
                startTime.setText(time);
                }
        };
        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeSet: hh:mm " + hourOfDay + ":" + minute);

                String time = hourOfDay + ":" + minute;
                endTime.setText(time);
                }
        };
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        startDate.setText(date);
        endDate.setText(date);
        theDate.setText(date);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        backToEventsToday.setOnClickListener(this);
        addEventBtn.setOnClickListener(this);

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = month + "/" + dayOfMonth + "/" + year;
                startDate.setText(date);
            }
        };
        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = month + "/" + dayOfMonth + "/" + year;
                endDate.setText(date);
            }
        };

    }

    public void onClick(View view) {
        if(view == backToEventsToday){
            //will open login
            Intent intent = new Intent(addEvent.this, EventsToday.class);
            intent.putExtra("date", date);
            startActivity(intent);
        }
        if(view == startDate) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Dialog,
                    startDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        if(view == endDate) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Dialog,
                    endDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        if(view == startTime) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    startTimeSetListener,
                    hour, minute, true);
            dialog.show();
        }
        if(view == endTime) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    endTimeSetListener,
                    hour, minute, true);
            dialog.show();
        }
        if(view == addEventBtn) {
            Toast.makeText(addEvent.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
            String userId = firebaseAuth.getCurrentUser().getUid();
            eventDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Events");
            String eventId = eventDB.push().getKey();
            String event_name = eventName.getText().toString().trim();
            String start_time = startTime.getText().toString().trim();
            String end_time = endTime.getText().toString().trim();
            String start_date = startDate.getText().toString().trim();
            String end_date = endDate.getText().toString().trim();
            String remarks_ = remarks.getText().toString().trim();
            Event event = new Event(event_name, start_time, end_time, start_date, end_date, remarks_, eventId);
            eventDB.child(eventId).setValue(event);
            Intent intent = new Intent(addEvent.this, EventsToday.class);
            intent.putExtra("date", date);
            startActivity(intent);
        }
    }
}

