package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class ViewPets extends AppCompatActivity {
    String receivePetID;
    DatabaseReference petReference;
    DatabaseReference userReference;
    private ImageView ivViewPets, ivViewPetsLocation;
    private TextView tvAboutViewPets, tvPetNameViewPets, tvPetAgeViewPets, tvPetBreedViewPets, tvPetGenderViewPets, tvViewPetsAddress;
    private Button btnCallViewPets, btnChatViewPets;
    String otherUserKey,otherUsername;
    String otherImage;
    String currentUser;
    String userPhone;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewPets.this, MainActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pets);

        ivViewPets = findViewById(R.id.ivViewPet);
        tvAboutViewPets = findViewById(R.id.tvAboutViewPets);
        tvPetNameViewPets = findViewById(R.id.tvPetNameViewPets);
        tvPetAgeViewPets = findViewById(R.id.tvPetAgeViewPets);
        tvPetBreedViewPets = findViewById(R.id.tvPetBreedViewPets);
        tvPetGenderViewPets = findViewById(R.id.tvPetGenderViewPets);
        btnCallViewPets = findViewById(R.id.btnCallViewPets);
        btnChatViewPets = findViewById(R.id.btnChatViewPets);
        ivViewPetsLocation = findViewById(R.id.ivViewPetsLocation);
        tvViewPetsAddress = findViewById(R.id.tvPetAddressViewPets);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        petReference = FirebaseDatabase.getInstance().getReference();
        receivePetID = getIntent().getStringExtra("view_pet_id");
        petReference.child("Approved_req").child(receivePetID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pet_Model pet = snapshot.getValue(Pet_Model.class);

                tvPetNameViewPets.setText(pet.getPetName());
                tvAboutViewPets.setText(pet.getPetAbout());
                tvPetAgeViewPets.setText(pet.getPetAge());
                tvPetBreedViewPets.setText(pet.getPetBreed());
                Picasso.get().load(pet.getImageUrl()).into(ivViewPets);
                tvPetGenderViewPets.setText(pet.getPetGender());
                String address = pet.getCity()+", "+pet.getState()+", "+pet.getCountry();
                tvViewPetsAddress.setText(address);
                otherUserKey = pet.getPetUser();
                FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel user = snapshot.getValue(UserModel.class);
                        userPhone= user.getUserMobile();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherUsername = snapshot.child(otherUserKey).child("userName").getValue().toString();
                otherImage = snapshot.child(otherUserKey).child("image").getValue().toString();
                if(firebaseUser != null) {
                    currentUser = snapshot.child(firebaseUser.getUid()).child("userName").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        ivViewPetsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewPets.this,ViewPetsLocation.class);
                i.putExtra("view_pet_id",receivePetID);
                startActivity(i);
            }
        });

        btnChatViewPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser != null) {
                    if (otherUsername != currentUser) {
                        Intent i = new Intent(ViewPets.this, ChatActivity.class);
                        i.putExtra("otherUsername", otherUsername);
                        i.putExtra("otherImage", otherImage);
                        i.putExtra("CurrentUser", currentUser);
                        i.putExtra("otherId", otherUserKey);
                        i.putExtra("userId", firebaseUser.getUid());
                        startActivity(i);
                        //finish();
                    } else {
                        Toast.makeText(ViewPets.this, "Not allowed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ViewPets.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCallViewPets.setOnClickListener(view -> {
            if(firebaseUser != null){
                if(currentUser != otherUsername){
                    dailPhone(userPhone);
                }else {
                    Toast.makeText(this, "Not allowed", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dailPhone(String userPhone) {
        String uri = "tel:"+userPhone.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}