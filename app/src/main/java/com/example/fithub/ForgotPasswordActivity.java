package com.example.fithub;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
public class ForgotPasswordActivity extends AppCompatActivity
{
    private EditText editEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        initializeUI();
        mAuth = FirebaseAuth.getInstance();
        btnResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = editEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Check email to reset your password", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset password email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initializeUI()
    {
        editEmail = findViewById(R.id.edit_reset_email);
        btnResetPassword = findViewById(R.id.reset_password);
    }
}
