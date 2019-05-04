package com.example.fithub;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fithub.ModelClasses.Weight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GraphWeightActivity extends AppCompatActivity {

    EditText weightValue;
    Button btnSubmit;
    Calendar calendar;
    FirebaseDatabase db;
    DatabaseReference dbRef;

    GraphView graphView;
    LineGraphSeries series;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M d");



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_weight);


        weightValue = (EditText) findViewById(R.id.et_updateWeight);
        btnSubmit = (Button) findViewById(R.id.b_submit_weight);
        graphView = (GraphView) findViewById(R.id.graphView);

        calendar = Calendar.getInstance();

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return simpleDateFormat.format(new Date((long)value));
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("User Weight");

        setListeners();

    }

    private void setListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = calendar.getTime();
                String id = dbRef.push().getKey();
                int weight = Integer.parseInt(weightValue.getText().toString());

                Weight newWeight = new Weight(date, weight);

                dbRef.child(id).setValue(newWeight);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int)dataSnapshot.getChildrenCount()];
                int index = 0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    Weight weight = myDataSnapshot.getValue(Weight.class);
                    try{
                        dp[index] = new DataPoint(weight.getDate(),weight.getWeight());
                        index++;
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
                try{
                    graphView.addSeries(series);
                }catch(Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}