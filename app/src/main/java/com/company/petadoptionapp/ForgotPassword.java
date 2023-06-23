package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button btnReset;
    private ProgressBar pbReset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(ForgotPassword.this,R.color.black));

        email = findViewById(R.id.etFPEmail);
        btnReset = findViewById(R.id.btnResetPassword);
        pbReset = findViewById(R.id.pbReset);

        auth = FirebaseAuth.getInstance();

        pbReset.setVisibility(View.GONE);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String mail = email.getText().toString();
        if(mail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        pbReset.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotPassword.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}