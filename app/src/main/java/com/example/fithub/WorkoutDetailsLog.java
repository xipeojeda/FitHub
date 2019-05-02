package com.example.fithub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.fithub.ModelClasses.workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkoutDetailsLog extends AppCompatActivity {

    private TextView workdat, worktype, wExercise, workReps;
    private BottomNavigationView nav;
    private Button logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details_log);
        initializeUI();

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_home:
                        Intent home = new Intent(WorkoutDetailsLog.this, MainActivity.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_log:
                        break;
                    case R.id.navigation_account:
                        Intent acct = new Intent(WorkoutDetailsLog.this, AccountActivity.class);
                        startActivity(acct);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
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
                myRef.child("Workouts").child(child).setValue(setLog());
            }
        });

    }

//comment
    private workout setLog()
    {
        String d;
        String type;
        String time;
        CharSequence r = workReps.getText();
        int repetitions;

        d = workdat.getText().toString();
        type = worktype.getText().toString();
        time = wExercise.getText().toString();
        repetitions = Integer.parseInt(r.toString());
        workout w = new workout(d,type,time,repetitions);
        return w;
    }

    private void initializeUI()
    {
        logger = findViewById(R.id.logBtn);
        workdat = findViewById(R.id.etDate);
        worktype = findViewById(R.id.type);
        wExercise =  findViewById(R.id.exercise);
        workReps = findViewById(R.id.reps);
        nav = findViewById(R.id.navigation);
    }
}
