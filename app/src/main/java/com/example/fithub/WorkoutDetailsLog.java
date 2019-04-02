package com.example.fithub;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WorkoutDetailsLog extends AppCompatActivity {

    TextView workdat, worktype, worktime;
    private Button logger;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details_log);
        logger = (Button) findViewById(R.id.logBtn);
        workdat =  (TextView) findViewById(R.id.date);
        worktype =  (TextView) findViewById(R.id.type);
        worktime =  (TextView) findViewById(R.id.time);



        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference();
        //final DatabaseReference childRef = myRef.child("Workouts");
        final String workoutDay = workdat.getText().toString();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                workout value = dataSnapshot.getValue(workout.class);
                Log.d("Val", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("on cancelled", "Failed to read value.", error.toException());
            }
        });

        logger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String child = workdat.getText().toString();
                myRef.child(child).setValue(setLog());
            }
        });

    }

//comment
    private workout setLog()
    {
        String d;
        String type;
        String time;

        d = workdat.getText().toString();
        type = worktype.getText().toString();
        time = worktime.getText().toString();
        workout w = new workout(d,type,time);
        return w;




    }
}
