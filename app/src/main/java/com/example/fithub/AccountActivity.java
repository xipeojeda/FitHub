package com.example.fithub;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fithub.ModelClasses.Weight;
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
    private TextView weight;
    private Button saveWeight;
    private  BottomNavigationView nav;
    private Uri filePath;
    private static int RESULT_LOAD_IMAGE = 72;
    private static final String TAG = "Delete User: ";
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);//setting xml
        initializeUI();//links local variables to activity xml variables
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = mAuth.getCurrentUser();

        for(UserInfo userI: user.getProviderData())
        {
            if(userI.getProviderId().equals("google.com"))
            {
                //if user is signed in with google account
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct != null)
                {
                    //getting information from google account and setting to display in TextViews
                    String personName = "Name: " + acct.getDisplayName();
                    String personEmail = "Email: " + acct.getEmail();
                    Uri uri = acct.getPhotoUrl();
                    //other texts fields such as birthdate and age are not included in google api
                    //they will be left out for google users

                    String user_id = user.getUid();
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference yourRef2 = myRef.child("User Weight").child(user_id);

                    uName.setText(personName);
                    email.setText(personEmail);
                    Picasso.get().load(uri).into(image);//setting google profile image to image (CircularImageView)

                    ValueEventListener eventListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Weight dWeight = dataSnapshot.child("Weight").getValue(Weight.class);
                            if(dWeight == null)//if no weight set to default
                            {
                                String def = "0.0";
                                weight.setText(def);
                            }
                            else{
                                String strWeight = "" + dWeight.getWeight();
                                weight.setText(strWeight);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    yourRef2.addListenerForSingleValueEvent(eventListener2);
                }
            }
            if(userI.getProviderId().equals("password"))
            {
                //gets unique user id
                String user_id = user.getUid();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                //where data lives
                DatabaseReference yourRef = myRef.child("User Information").child(user_id);
                DatabaseReference yourRef2 = myRef.child("User Weight").child(user_id);

                ValueEventListener eventListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Weight dWeight = dataSnapshot.child("Weight").getValue(Weight.class);
                        if(dWeight == null)//if no weight set default
                        {
                            String def = "0.0";
                            weight.setText(def);
                        }
                        else{
                            String strWeight = "" + dWeight.getWeight();
                            weight.setText(strWeight);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                yourRef2.addListenerForSingleValueEvent(eventListener2);

                //if user registered using email and password retrieve information from firebase database
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        //setting local variables to data from firebase database
                        String fullName = "Name: " + dataSnapshot.child("First Name").getValue(String.class)+ " " + dataSnapshot.child("Last Name").getValue(String.class);
                        String eMail = "Email: " + dataSnapshot.child("Email").getValue(String.class);
                        String dateB = "Date of Birth: " + dataSnapshot.child("Date of Birth").getValue(String.class);
                        String uAge = "Age: " + dataSnapshot.child("Age").getValue(Integer.class);
                        //setting textview to new values received from firebase database
                        uName.setText(fullName);
                        email.setText(eMail);
                        dob.setText(dateB);
                        age.setText(uAge);
                       storageReference.child("images/" + user_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               Picasso.get().load(uri).into(image);//load image to cache and make it image (CircularImageView)

                           }//if image fails to load display error
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(AccountActivity.this,"Firebase Profile Image Not Loaded", Toast.LENGTH_SHORT).show();
                           }
                       });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                yourRef.addListenerForSingleValueEvent(eventListener);
            }
        }
        //if user clicks on profile image start selectImage method
        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
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
                        Intent log = new Intent(AccountActivity.this, WorkoutDetailsLogActivity.class);
                        startActivity(log);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_account: //keeps user at current screen (account screen)
                        break;
                }
                return false;
            }
        });
        /*
         inflates  3 dot menu, and handles the different options
         */
        //setting up 3 dot menu options
        dMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                popup.inflate(R.menu.account_options);//inflate 3 dot menu
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())

                        {
                            case R.id.upload:
                                uploadImage(); //runs upload method
                                return true;
                            case R.id.logout:
                                mAuth.signOut();//logs user out and brings them to login screen
                                Intent go2Login = new Intent(AccountActivity.this, LoginActivity.class);
                                startActivity(go2Login);
                                finish();
                                return true;
                            case R.id.delete:
                                deleteUser();//runs delete method
                                return true;
                            default:
                                return false; //exits menu
                        }
                    }
                });
            }
        });

        saveWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserWeight();
            }
        });
    }

    /*Initializes class variables and links them to xml
    @param NA
    @returns NA*/
    public void initializeUI()
    {
        dMenu = findViewById(R.id.dotMenu);
        image = findViewById(R.id.profileImage);
        uName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.birthday);
        age = findViewById(R.id.age);

        //only edit text field i want editable
        weight = findViewById(R.id.userWeight);
        weight.setGravity(Gravity.CENTER_HORIZONTAL);
        saveWeight = findViewById(R.id.weightButton);

        //making edit text fields uneditable
        uName.setKeyListener(null);
        email.setKeyListener(null);
        dob.setKeyListener(null);
        age.setKeyListener(null);
        //creating bottom navigation
        nav = findViewById(R.id.navigation);
    }
    /*
        Starts new intent that calls startActivityFor result to allow the user to select an image
        @param NA
        @returns NA
     */
    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");//setting path
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_LOAD_IMAGE);
    }
    /*
    fetches filePath of an image and sets it image (CircularImageView to that image)
    @param int requestCode, int resultCode, Intent data
    @returns NA
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                //setting image from phone to image (CircularImageView)
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }
    /*
    uploads the user image to firebase storage
    @param NA
    @return NA
     */
    private void uploadImage()
    {
        String user_id = user.getUid();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/").child(user_id);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override//if upload is successful
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AccountActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {//if not print out error message
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

    /*deletes user, on click of button prompt user if they are sure if no close dialog yes delete account and send to login page
    @param NA
    @returns NA
    */
    public void deleteUser()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");//if user selects yes delete account
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    //takes user back to login page
                                    Log.d(TAG, "User Account Deleted");
                                    Intent go2login = new Intent(AccountActivity.this, LoginActivity.class);
                                    startActivity(go2login);
                                }
                            }
                        });
            }
        });
        //if user selects NO dismiss the prompt
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /*
    allows user to log weight to db
    @param NA
    @return NA
     */
    public void logUserWeight()
    {
        double wght = 0.0;
        String getDouble = weight.getText().toString();
        try
        {
            wght = Double.parseDouble(getDouble);

        }catch(NumberFormatException e){}

        if(TextUtils.isEmpty(getDouble))
        {
            Toast.makeText(getApplicationContext(), "Please Enter Weight...", Toast.LENGTH_LONG).show();
            return;
        }

        String user_id = user.getUid();
        Weight w = new Weight(wght);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        //where data lives
        DatabaseReference yourRef = myRef.child("User Weight").child(user_id);
        yourRef.child("Weight").setValue(w);
        Toast.makeText(getApplicationContext(), "Weight Logged", Toast.LENGTH_LONG).show();
    }

}
