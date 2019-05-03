package com.example.fithub;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fithub.ModelClasses.workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayWorkoutsActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference myRef;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workouts);

        //instantiate the objects
        listView = (ListView) findViewById(R.id.listview);
        db=FirebaseDatabase.getInstance();
        myRef = db.getReference("Workouts");

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
        for(DataSnapshot ds: dataSnapshot.getChildren())
        {
            workout w = new workout();
            w.setDate(ds.getValue(workout.class).getDate());
            w.setTime(ds.getValue(workout.class).getExercise());
            w.setType(ds.getValue(workout.class).getType());
            w.setReps(ds.getValue(workout.class).getReps());
            String x = Integer.valueOf(w.getReps()).toString();
            array.add(w.getDate());
            array.add(w.getType());
            array.add(w.getExercise());
            array.add(x);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,array);
        listView.setAdapter(adapter);

    }
}
