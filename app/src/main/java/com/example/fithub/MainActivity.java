package com.example.fithub;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fithub.SensorClasses.StepDetector;
import com.example.fithub.SensorClasses.StepListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private BottomNavigationView nav;
    private TextView tvSteps;
    private TextView stepCounter;
    private Button btnStart;
    private Button btnStop;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String user_id = user.getUid();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("User Step Count").child(user_id);

        initializeUI();
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        //When start button is pressed start recording steps by registering sensorManager
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSteps = 0;//step counter
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                Toast.makeText(MainActivity.this, "Step Counter Started", Toast.LENGTH_SHORT).show();
            }
        });
        //When stop button is pressed stop recording steps by unregistering sensorManager
        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myRef.push().setValue(numSteps);//pushes steps to firebase database
                sensorManager.unregisterListener(MainActivity.this);
                Toast.makeText(MainActivity.this, "Step Counter Stopped", Toast.LENGTH_SHORT).show();

            }
        });
        //handles navigation, when something other than current page is pressed start that
        //activity
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_home://home
                        break;
                    case R.id.navigation_log: //workout logger
                        Intent log = new Intent(MainActivity.this, WorkoutDetailsLogActivity.class);
                        startActivity(log);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_account: // account page
                        Intent acct = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(acct);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    //Takes account for steps and displays the counter
    public void step(long timeNs) {
        numSteps++;
        String stepDisplay = Integer.toString(numSteps);
        stepCounter.setText(stepDisplay);
    }
    /*Initializes class variables and links them to xml
    @param NA
    @returns NA*/
    private void initializeUI()
    {
        tvSteps = findViewById(R.id.stepView);
        btnStart = findViewById(R.id.buttonStart);
        btnStop = findViewById(R.id.buttonStop);
        nav = findViewById(R.id.navigation);
        stepCounter = findViewById(R.id.txvSteeps);
    }
}