package com.example.fithub;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.android.gms.fitness.data.DataPoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WeightLogger extends AppCompatActivity {
    EditText date, weight;
    Button log;
    GraphView graphView;
    LineGraphSeries series;
    FirebaseDatabase db;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_logger);

        date = (EditText) findViewById(R.id.etDate);
        weight = (EditText) findViewById(R.id.etWeight);
        Button log = (Button) findViewById(R.id.logBtn);
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Weights");
        graphView = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries();
        graphView.addSeries(series);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String child = date.getText().toString();
                myRef.push().setValue(setLog());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[((int) dataSnapshot.getChildrenCount())];
                int x=0;

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Weight value = ds.getValue(Weight.class);
                    Date date1 =null;

                    try {
                        date1 = new SimpleDateFormat("mm/dd/yy"   ).parse(value.getDate());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    dp[x] = new DataPoint(date1, value.getWeight());
                    x++;
                }

                series.resetData(dp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.w("on cancelled", "Failed to read value.", databaseError.toException());

            }
        });


    }

    private Weight setLog()
    {   double wght;
        String inputDate = date.getText().toString();
        String getDouble = weight.getText().toString();
        wght = Double.parseDouble(getDouble);

        Weight w = new Weight(inputDate, wght);
        return w;

    }


}
