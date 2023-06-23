package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class ViewLostPets extends AppCompatActivity {
    String receivePetID;
    DatabaseReference petReference;
    private ImageView ivViewLostPet;
    private TextView tvAboutViewLostPets, tvPetAgeViewLostPets,
            tvPetBreedViewLostPets, tvPetGenderViewLostPets, tvPetAddressViewLostPets;
    private Button btnCallViewLostPets ;
    String currentUser;

    String userPhone,otherUsername;

    FirebaseUser firebaseUser;




    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewLostPets.this, MainActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lost_pets);
        ivViewLostPet = findViewById(R.id.ivViewLostPet);

        tvAboutViewLostPets = findViewById(R.id.tvAboutViewLostPets);
        tvPetAgeViewLostPets = findViewById(R.id.tvPetAgeViewLostPets);
        tvPetBreedViewLostPets = findViewById(R.id.tvPetBreedViewLostPets);
        tvPetGenderViewLostPets = findViewById(R.id.tvPetGenderViewLostPets);
        tvPetAddressViewLostPets = findViewById(R.id.tvPetAddressViewLostPets);

        btnCallViewLostPets = findViewById(R.id.btnCallViewLostPets);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            otherUsername = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        petReference = FirebaseDatabase.getInstance().getReference();
        receivePetID = getIntent().getStringExtra("view_lost_pet_id");

        petReference.child("Lost_Approved_req").child(receivePetID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pet_Model pet = snapshot.getValue(Pet_Model.class);

                tvAboutViewLostPets.setText(pet.getPetAbout());
                tvPetAgeViewLostPets.setText(pet.getPetAge());
                tvPetBreedViewLostPets.setText(pet.getPetBreed());
                Picasso.get().load(pet.getImageUrl()).into(ivViewLostPet);
                tvPetGenderViewLostPets.setText(pet.getPetGender());
                tvPetAddressViewLostPets.setText(pet.getPetAddress());
                currentUser = pet.getPetUser();
                FirebaseDatabase.getInstance().getReference().child("NGO").child(currentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NGO_Model user = snapshot.getValue(NGO_Model.class);
                        userPhone= user.getNgoPhone();
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
        btnCallViewLostPets.setOnClickListener(view -> {
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
