package com.example.fithub;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fithub.logger.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//
public class AccountActivity extends AppCompatActivity {
    public ActionBar toolbar;
    private FirebaseAuth mAuth;
    private Button logoutBtn;
    private Button deleteAcct;
    private static final String TAG = "Delete User: ";
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initializeUI();
        mAuth = FirebaseAuth.getInstance();

        //creating bottom navigation
        toolbar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar.setTitle("Account");

        //sign out user and bring to log in page
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent go2Login = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(go2Login);
                finish();
            }
        });

        deleteAcct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
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

    //initializes user modifiable buttons/text
    public void initializeUI()
    {
        logoutBtn = findViewById(R.id.logout);
        deleteAcct = findViewById(R.id.deleteAccount);
    }

    //deletes user, on click of button
    //prompt user if they are sure
    //if no close dialog
    //yes delete account and send to login page
    public void deleteUser()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Log.d(TAG, "User Account Deleted");
                                    Intent go2login = new Intent(AccountActivity.this, LoginActivity.class);
                                    startActivity(go2login);
                                }
                            }
                        });
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
