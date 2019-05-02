package com.example.fithub;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fithub.logger.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private CircleImageView image;
    private ImageButton dMenu;
    private TextView uName;
    private TextView email;
    private TextView dob;
    private TextView age;
    private  BottomNavigationView nav;
    private Uri filePath;
    private static int RESULT_LOAD_IMAGE = 72;
    private static final String TAG = "Delete User: ";
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initializeUI();//links local variables to activity xml variables
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = mAuth.getCurrentUser();

        for(UserInfo userI: user.getProviderData())
        {
            if(userI.getProviderId().equals("google.com"))
            {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct != null)
                {
                    String personName = "Name: " + acct.getDisplayName();
                    String personEmail = "Email: " + acct.getEmail();
                    Uri uri = acct.getPhotoUrl();

                    uName.setText(personName);
                    email.setText(personEmail);
                    Picasso.get().load(uri).into(image);
                }
            }
            if(userI.getProviderId().equals("password"))
            {
                String user_id = user.getUid();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference yourRef = myRef.child("User Information").child(user_id);
                //if user registered using email and password retrieve information from firebase database
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        String fullName = "Name: " + dataSnapshot.child("First Name").getValue(String.class)+ " " + dataSnapshot.child("Last Name").getValue(String.class);
                        String eMail = "Email: " + dataSnapshot.child("Email").getValue(String.class);
                        String dateB = "Birthday: " + dataSnapshot.child("Date of Birth").getValue(String.class);
                        String uAge = "Age: " + dataSnapshot.child("Age").getValue(Integer.class);
                        //setting textview to new values received from firebase database
                        uName.setText(fullName);
                        email.setText(eMail);
                        dob.setText(dateB);
                        age.setText(uAge);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                yourRef.addListenerForSingleValueEvent(eventListener);
            }
        }

        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
                uploadImage();
            }
        });
        //switch statement that controls bottom navigation activities
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_home://takes user to home page
                        Intent home = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_log: //takes user to log screen
                        Intent log = new Intent(AccountActivity.this, WorkoutDetailsLog.class);
                        startActivity(log);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_account: //keeps user at current screen (account screen)
                        break;
                }
                return false;
            }
        });
        //setting up 3 dot menu options
        dMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                popup.inflate(R.menu.account_options);
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.logout:
                                mAuth.signOut();//logs user out and brings them to login screen
                                Intent go2Login = new Intent(AccountActivity.this, LoginActivity.class);
                                startActivity(go2Login);
                                finish();
                                return true;
                            case R.id.delete:
                                deleteUser();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    //initializes user modifiable buttons/text
    public void initializeUI()
    {
        dMenu = findViewById(R.id.dotMenu);
        image = findViewById(R.id.profileImage);
        uName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.birthday);
        age = findViewById(R.id.age);

        //creating bottom navigation
        nav = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void uploadImage()
    {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AccountActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AccountActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress+"%");
                        }
                    });
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
