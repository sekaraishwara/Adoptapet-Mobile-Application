package com.company.petadoptionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Profile_Fragment extends Fragment {

    public Profile_Fragment() {
        // Required empty public constructor
    }

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    private CardView cvLogin, cvManagePets, cvLogout;
    private TextView tvLoginSignup;
    private ImageView ivProfilePhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile__fargment, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Users");

        cvLogin = view.findViewById(R.id.cvLogin);
        cvManagePets = view.findViewById(R.id.cvManagePets);
        cvLogout = view.findViewById(R.id.cvLogout);
        tvLoginSignup = view.findViewById(R.id.tvLoginSignup);
        ivProfilePhoto = view.findViewById(R.id.profilePic);


        if(user != null){
            tvLoginSignup.setText("Loading...");
            String uid = user.getUid();
            ref.child(uid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    String name = user.userName;
                    String mail = user.userMail;
                    String mobile = user.userMobile;
                    String image = user.image;


                    tvLoginSignup.setText(name);
                    if (!image.equals("null")) {
                        Picasso.get().load(image).into(ivProfilePhoto);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            cvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(),UserProfile.class));
                }
            });

            cvManagePets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(),ManagePets.class));
                }
            });

            cvLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth.signOut();
                    Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            });

        }else{

            cvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });

            cvManagePets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
                }
            });

            cvLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}