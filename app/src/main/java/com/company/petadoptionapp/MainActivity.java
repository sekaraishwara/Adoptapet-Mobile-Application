package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.company.petadoptionapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    LostPetFragment lostPetFragment = new LostPetFragment();
    MessageFragment messageFragment = new MessageFragment();
    Profile_Fragment profileFragment = new Profile_Fragment();
    DatabaseReference ref;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ref = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            String uid = FirebaseAuth.getInstance().getUid();
            ref.child("NGO").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(uid)){
                        startActivity(new Intent(MainActivity.this, HomepageNGO.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().findItem(R.id.placeholder).setEnabled(false);
        fab = findViewById(R.id.fabAddPet);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeID:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.lostPet:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,lostPetFragment).commit();
                        return true;
                    case R.id.message:
                        if(user != null) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, messageFragment).commit();
                            return true;
                        }else{
                            Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                        }
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                }
                return false;
            }
        });

        if(user != null){
            fab.setOnClickListener(view -> {
                startActivity(new Intent(MainActivity.this,AddPets.class));
            });
        }else{
            fab.setOnClickListener(view -> {
                Toast.makeText(this, "You cannot add pets without Login", Toast.LENGTH_SHORT).show();
            });
        }

    }
}