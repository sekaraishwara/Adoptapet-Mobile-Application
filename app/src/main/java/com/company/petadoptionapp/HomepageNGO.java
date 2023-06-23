package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class HomepageNGO extends AppCompatActivity {

    FloatingActionButton fabNGO;
    BottomNavigationView bnvNGO;
    HomeFragmentNGO homeFragmentNGO = new HomeFragmentNGO();
    ProfileFragmentNGO profileFragmentNGO = new ProfileFragmentNGO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_ngo);
        bnvNGO = findViewById(R.id.bottomNavigationViewNGO);
        fabNGO = findViewById(R.id.fabAddPetNGO);
        bnvNGO.setBackground(null);
        bnvNGO.getMenu().findItem(R.id.placeholderNGO).setEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerNGO,homeFragmentNGO).commit();

        bnvNGO.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeIDNGO:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerNGO,homeFragmentNGO).commit();
                        return true;

                    case R.id.profileNGO:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerNGO,profileFragmentNGO).commit();
                        return true;
                }
                return false;
            }
        });

        fabNGO.setOnClickListener(view -> {
            startActivity(new Intent(this,AddPetsNGO.class));
        });
    }
}