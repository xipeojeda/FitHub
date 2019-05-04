package com.example.fithub;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fithub.ModelClasses.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The type Display workouts activity.
 */
public class DisplayWorkoutsActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workouts);

        //instantiate the objects
        listView = findViewById(R.id.listview);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String user_id = user.getUid();
        myRef = db.getReference("Workouts").child(user_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //what to do when database is initially called and upon further calls
    private void showData(DataSnapshot dataSnapshot) {
        //sets an array list of workout inputs
        ArrayList<String> array = new ArrayList<>();
        /**
         * Will iterate through the current database references snapshot and fill the arraylist with
         * the getters from the objects that are stored in the database
         */
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Workout w = new Workout();
            w.setDate(ds.getValue(Workout.class).getDate());
            w.setTime(ds.getValue(Workout.class).getExercise());
            w.setType(ds.getValue(Workout.class).getType());
            w.setReps(ds.child("reps").getValue(Integer.class));
            String x = Integer.valueOf(w.getReps()).toString();
            String userWDate = "Date: " + w.getDate() + ", Type: " + w.getType() + ", Exercise: " + w.getExercise() + ", Repetitions: " + x;
            array.add(userWDate);
        }
        //Works with the xml adapter in order to display array content
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,array);
        listView.setAdapter(adapter);
    }
}
