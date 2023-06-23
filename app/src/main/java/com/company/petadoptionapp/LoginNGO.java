package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginNGO extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvRegister, tvForgotPassword;
    private Button btnLoginNGO;
    private ProgressBar pbLoginNGO;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ngo);

        getSupportActionBar().hide();

        etEmail = findViewById(R.id.etLoginEmailNGO);
        etPassword = findViewById(R.id.etLoginPasswordNGO);
        tvRegister = findViewById(R.id.tvRegisterNGO);
        tvForgotPassword = findViewById(R.id.tvForgotPasswordNGO);
        btnLoginNGO = findViewById(R.id.btnLoginNGO);
        pbLoginNGO = findViewById(R.id.pbLoginNGO);
        pbLoginNGO.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        btnLoginNGO.setOnClickListener(view -> {
            pbLoginNGO.setVisibility(View.VISIBLE);
            NGOlogin();
        });

        tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpNGO.class));
        });

        tvForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(this,ForgotPassword.class));
        });

    }

    private void NGOlogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter valid Email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            etPassword.setError("Wrong Password");
            etPassword.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pbLoginNGO.setVisibility(View.GONE);
                Toast.makeText(LoginNGO.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginNGO.this,HomepageNGO.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbLoginNGO.setVisibility(View.GONE);
                Toast.makeText(LoginNGO.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}