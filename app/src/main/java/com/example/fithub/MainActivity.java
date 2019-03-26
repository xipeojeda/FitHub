package com.example.fithub;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;

    Button registerBtn, loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        //Create the Google Api Client
        mClient = new GoogleApiClient.Builder(this)
        //select the Fitness Api
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                //Specify Scopes of access
                .addScope(FitnessScopes.SCOPE_ACTIVITY_READ)
                .addScope(FitnessScopes.SCOPE_BODY_READ_WRITE)
                //provide callbacks
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        registerBtn = findViewById(R.id.register);
        loginBtn = findViewById(R.id.login);
    }
}