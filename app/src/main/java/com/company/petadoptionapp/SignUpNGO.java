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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpNGO extends AppCompatActivity {

    private EditText etNameNGO, etEmailNGO, etPasswordNGO, etPhoneNGO, etRegNoNGO;
    private Button btnSignUpNGO;
    private ProgressBar pb;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_ngo);
        getSupportActionBar().hide();

        etNameNGO = findViewById(R.id.etSignUpNameNGO);
        etEmailNGO = findViewById(R.id.etSignUpEmailNGO);
        etPasswordNGO = findViewById(R.id.etSignUpPasswordNGO);
        etPhoneNGO = findViewById(R.id.etSignUpPhoneNGO);
        etRegNoNGO = findViewById(R.id.etSignUpRegNoNGO);
        btnSignUpNGO = findViewById(R.id.btnSignUpNGO);
        pb = findViewById(R.id.pbSignUpNGO);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();

        pb.setVisibility(View.GONE);

        btnSignUpNGO.setOnClickListener(view -> {
            pb.setVisibility(View.VISIBLE);
            registerNGO();
        });
    }

    private void registerNGO() {

        String name = etNameNGO.getText().toString();
        String email = etEmailNGO.getText().toString();
        String password = etPasswordNGO.getText().toString();
        String regNo = etRegNoNGO.getText().toString();
        String phone = etPhoneNGO.getText().toString();

        if(name.isEmpty()){
            etNameNGO.setError("Enter Name");
            etNameNGO.requestFocus();
            return;
        }

        if(email.isEmpty()){
            etEmailNGO.setError("Enter Email");
            etEmailNGO.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailNGO.setError("Please provide valid Email");
            etEmailNGO.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPasswordNGO.setError("Enter Password");
            etPasswordNGO.requestFocus();
            return;
        }

        if(password.length() < 8){
            etPasswordNGO.setError("At least 8 characters required");
            etPasswordNGO.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            etPhoneNGO.setError("Enter Phone Number");
            etPhoneNGO.requestFocus();
            return;
        }

        if(phone.length() != 12){
            etPhoneNGO.setError("Invalid Phone Number");
            etPhoneNGO.requestFocus();
            return;
        }

        if(regNo.isEmpty()){
            etRegNoNGO.setError("Enter Postal Code");
            etRegNoNGO.requestFocus();
            return;
        }

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                NGO_Model ngo = new NGO_Model(name,email,regNo,phone);
                reference.child("NGO").child(auth.getUid()).setValue(ngo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignUpNGO.this, "Rescuer Registered Successfully", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        startActivity(new Intent(SignUpNGO.this,LoginNGO.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpNGO.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpNGO.this, "Something Went Wrong in Registration", Toast.LENGTH_SHORT).show();
            }
        });
    }
}