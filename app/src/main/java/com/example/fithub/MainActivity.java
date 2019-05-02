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

import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private GoogleApiClient mClient;
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    TextView TvSteps;
    TextView BtnStart;
    TextView BtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.navigation);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_home:
                        Intent pedo = new Intent(MainActivity.this, PedometerActivity.class);
                        startActivity(pedo);
                        overridePendingTransition(0,0);
                        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        simpleStepDetector = new StepDetector();
                        simpleStepDetector.registerListener(this);

                        TvSteps = (TextView) findViewById(R.id.tv_steps);
                        BtnStart = (Button) findViewById(R.id.btn_start);
                        BtnStop = (Button) findViewById(R.id.btn_stop);

                        BtnStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {

                                numSteps = 0;
                                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                            }
                        });


                        BtnStop.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                sensorManager.unregisterListener(MainActivity.this);

                            }
                        });
                        break;
                    case R.id.navigation_log:
                        Intent log = new Intent(MainActivity.this, WorkoutDetailsLog.class);
                        startActivity(log);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_account:
                        Intent acct = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(acct);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
        //Create the Google Api Client
     /*   mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                //Specify Scopes of access
                //provide callbacks
                .addScope(SCOPE_ACTIVITY_READ)
                .addScope(SCOPE_ACTIVITY_READ_WRITE)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();*/

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
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
    }
}