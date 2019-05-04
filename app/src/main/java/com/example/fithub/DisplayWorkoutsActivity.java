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

    private void showData(DataSnapshot dataSnapshot) {
        ArrayList<String> array = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Workout w = new Workout();
            w.setDate(ds.getValue(Workout.class).getDate());
            w.setTime(ds.getValue(Workout.class).getExercise());
            w.setType(ds.getValue(Workout.class).getType());
            w.setReps(ds.child("reps").getValue(Integer.class));
            String x = Integer.valueOf(w.getReps()).toString();
            array.add(w.getDate());
            array.add(w.getType());
            array.add(w.getExercise());
            array.add("Repetitions: " + x);
        }

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,array);
        listView.setAdapter(adapter);
    }
}
