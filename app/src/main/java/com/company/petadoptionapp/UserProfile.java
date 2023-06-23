package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    private ImageView ivProfile;
    private TextView profileName, profileGender, profileDOB, profileMobile, profileEmail;
    private CardView cvProfileInfo;
    private ProgressBar pb;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivProfile = findViewById(R.id.ivProfilePhoto);
        profileName = findViewById(R.id.tvProfileName);
        profileMobile = findViewById(R.id.tvProfileMobile);
        profileEmail = findViewById(R.id.tvProfileEmail);
        cvProfileInfo = findViewById(R.id.cvProfileInfo);
        pb = findViewById(R.id.progressBar2);

        cvProfileInfo.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");

        String uid = FirebaseAuth.getInstance().getUid();

        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);

                profileName.setText(user.getUserName());
                profileMobile.setText(user.getUserMobile());
                profileEmail.setText(user.getUserMail());
                String image = user.getImage();
                Picasso.get().load(image).into(ivProfile);

                cvProfileInfo.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}