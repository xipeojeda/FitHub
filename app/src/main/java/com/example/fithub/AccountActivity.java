package com.example.fithub;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class AccountActivity extends AppCompatActivity {
    private ActionBar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //creating bottom navigation
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar.setTitle("Account");

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_home:
                        Intent home = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_log:
                        Intent log = new Intent(AccountActivity.this, WorkoutDetailsLog.class);
                        startActivity(log);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_account:
                        break;
                }
                return false;
            }
        });
    }

}
