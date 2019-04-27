package com.example.fithub;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CircleImageView image;
    private Button logoutBtn;
    private Button deleteAcct;
    private static final int SELECT_PICTURE = 0;
    private static int RESULT_LOAD_IMAGE = 1;
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
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);

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

        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
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
        image = findViewById(R.id.profileImage);
        image.setImageResource(R.drawable.defaultuser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


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
