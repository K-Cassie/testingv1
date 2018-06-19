package com.example.admin.testingv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class EventsToday extends AppCompatActivity implements View.OnClickListener{

    private TextView theDate;
    private Button backToProfile;
    private Button addEvent;
    private FirebaseAuth firebaseAuth;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_today);

        theDate = (TextView) findViewById(R.id.theDate);
        addEvent = (Button) findViewById(R.id.addEvent);
        backToProfile = (Button) findViewById(R.id.backToProfile);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent incomingIntent = getIntent();
        date = incomingIntent.getStringExtra("date");

        theDate.setText(date);

        backToProfile.setOnClickListener(this);
        addEvent.setOnClickListener(this);

    }

    public void onClick(View view) {
        if (view == backToProfile) {
            //will open login
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (view == addEvent) {
            Intent intent = new Intent(EventsToday.this, addEvent.class);
            intent.putExtra("date", date);
            startActivity(intent);
        }
    }
}
