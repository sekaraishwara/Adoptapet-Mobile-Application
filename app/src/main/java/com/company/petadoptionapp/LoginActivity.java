package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private TextView tvRegister,tvForgotPassword, tvNGOLogin;
    private EditText etLoginEmail, etLoginPass;
    private Button btnLogin;
    private ProgressBar pbLogin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.black));

        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPass = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        pbLogin = findViewById(R.id.pbLogin);
        tvNGOLogin = findViewById(R.id.tvNGOLogin);

        auth = FirebaseAuth.getInstance();

        pbLogin.setVisibility(View.INVISIBLE);

        tvNGOLogin.setOnClickListener(view -> {
            startActivity(new Intent(this,LoginNGO.class));
        });

        tvForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(this,ForgotPassword.class));
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, UserSignUpActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    private void userLogin(){
        String mail = etLoginEmail.getText().toString().trim();
        String pass = etLoginPass.getText().toString().trim();

        if(mail.isEmpty()){
            etLoginEmail.setError("Email can't be empty");
            etLoginEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            etLoginEmail.setError("Please provide valid Email");
            etLoginEmail.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            etLoginPass.setError("Password can't be empty");
            etLoginPass.requestFocus();
            return;
        }
        if(pass.length() < 8){
            etLoginPass.setError("Wrong Password");
            etLoginPass.requestFocus();
            return;
        }

        pbLogin.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.GONE);
                }
            }
        });

    }
}