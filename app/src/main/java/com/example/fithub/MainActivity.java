package com.example.fithub;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private GoogleApiClient mClient;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating bottom navigation
        toolbar = getSupportActionBar();
        BottomNavigationView nav = findViewById(R.id.navigation);
        toolbar.setTitle("FitHub");

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_home:
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




}