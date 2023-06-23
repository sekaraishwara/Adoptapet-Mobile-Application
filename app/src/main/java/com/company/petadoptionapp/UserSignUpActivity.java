package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

public class UserSignUpActivity extends AppCompatActivity {

    ImageView ivProfile;
    TextView loginText;
    EditText etMail, etPassword, etConfirmPassword, tvName, tvMobile;
    Button btnSignUp;
    ProgressBar progressBarSU;

    private boolean imageControl = false;


    Uri imageUri;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        ivProfile = findViewById(R.id.ivUploadPhoto);
        tvName = findViewById(R.id.tvSUpName);
        tvMobile = findViewById(R.id.tvSUpMobile);
        etConfirmPassword = findViewById(R.id.etSignUpConfirmPassword);
        etPassword = findViewById(R.id.etSignUpPassword);
        etMail = findViewById(R.id.etSignUpEmail);
        btnSignUp = findViewById(R.id.btnLogin);
        progressBarSU = findViewById(R.id.progressBarSU);
        loginText = findViewById(R.id.loginText);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressBarSU.setVisibility(View.GONE);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSignUpActivity.this, LoginActivity.class));
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }

        });

    }


    public void registerUser() {

        String userMobile = tvMobile.getText().toString().trim();
        String userName = tvName.getText().toString().trim();
        String userMail = etMail.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String image = etConfirmPassword.getText().toString().trim();


        UserModel userModel = new UserModel(userName, userMobile, userPassword , userMail, image);
        databaseReference.child(userName).setValue(userModel);

        if (userMobile.isEmpty()) {
            tvMobile.setError("Phone number can't be empty");
            tvMobile.requestFocus();
            return;
        }

            if (userMail.isEmpty()) {
                etMail.setError("Email can't be empty");
                etMail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
                etMail.setError("Please provide valid Email");
                etMail.requestFocus();
                return;
            }

            if (userPassword.isEmpty()) {
                etPassword.setError("Password can't be empty");
                etPassword.requestFocus();
                return;
            }

            if (userPassword.length() < 8) {
                etPassword.setError("At least 8 character required");
                etPassword.requestFocus();
                return;
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.setError("Confirm Password can't be empty");
                etConfirmPassword.requestFocus();
                return;
            }

            if (!userPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Confirm Password is not matching with Password");
                etConfirmPassword.requestFocus();
                return;
            }

            progressBarSU.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(userMail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (imageControl) {
                            UUID randomID = UUID.randomUUID();
                            String imageName = "images/" + randomID + ".jpg";
                            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            UserModel users = new UserModel(userName, userMobile, userMail.toString());
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(UserSignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                                progressBarSU.setVisibility(View.GONE);
                                                                Intent intent = new Intent(UserSignUpActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(UserSignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                                progressBarSU.setVisibility(View.GONE);
                                                            }
                                                        }
                                                    });
                                        }
                                    });}
                            });
                        } else {
                            UserModel users = new UserModel(userName, userMobile, userMail.toString());
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(UserSignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                progressBarSU.setVisibility(View.GONE);
                                                FirebaseDatabase.getInstance().getReference("Users");
                                                Intent intent = new Intent(UserSignUpActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(UserSignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                progressBarSU.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                    }
                }
            });

        }

        //When user click on upload photo this method will run
        public void imageChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        }

        //Checks whether user selected any image or not
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(ivProfile);
                imageControl = true;
            } else {
                imageControl = false;
            }
        }
    }
