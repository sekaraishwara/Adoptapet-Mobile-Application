package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.company.petadoptionapp.databinding.ActivityUpdatePetLocationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePetLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityUpdatePetLocationBinding binding;

    private SearchView svMapUpdate;
    private Button btnUpdateLocation;
    private Boolean searchClick = false;
    private List<Address> addressesList;
    private Address address;
    private final float DEFAULT_ZOOM = 15f;
    private Marker petMarker;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String petID;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdatePetLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        svMapUpdate = (SearchView) findViewById(R.id.svMapUpdate);
        btnUpdateLocation = findViewById(R.id.btnUpdateLocation);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        Intent intent = getIntent();
        petID = intent.getStringExtra("petID");

        displayLocation(petID);

        svMapUpdate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = svMapUpdate.getQuery().toString();
                if (location != null || !location.equals("")) {
                    searchClick = true;
                    Geocoder geocoder = new Geocoder((UpdatePetLocation.this));
                    try {
                        addressesList = geocoder.getFromLocationName(location, 1);
                        address = addressesList.get(0);
                        petMarker.setPosition(new LatLng(address.getLatitude(),address.getLongitude()));
                        moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM);

                    } catch (IOException e) {

                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnUpdateLocation.setOnClickListener(view -> {
            if(searchClick){
                updateLocation(petID, address);
            }else{
                Toast.makeText(this, "Updated Pet added for approval", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdatePetLocation.this,MainActivity.class));
            }
            send_for_approvel(ref.child("Approved_req").child(petID),reference.child("Approval_req").child(petID));
            startActivity(new Intent(UpdatePetLocation.this,MainActivity.class));
        });
    }

    private void updateLocation(String petID, Address address) {


        Double Latitude = address.getLatitude();
        Double Longitude = address.getLongitude();
        String City = address.getLocality();
        String State = address.getAdminArea();
        String Country = address.getCountryName();

        ref.child("Approved_req").child(petID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("Longitude").setValue(Longitude);
                snapshot.getRef().child("Latitude").setValue(Latitude);
                snapshot.getRef().child("City").setValue(City);
                snapshot.getRef().child("State").setValue(State);
                snapshot.getRef().child("Country").setValue(Country);
                insertCountry(address.getCountryName());
                insertCity(address.getLocality());
                insertState(address.getAdminArea());

                Toast.makeText(UpdatePetLocation.this, "Updated Pet added for Approval", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(UpdatePetLocation.this,MainActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void insertState(String adminArea) {
        ref.child("State").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(adminArea)){
                    ref.child("State").child(adminArea).setValue(adminArea);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insertCity(String locality) {
        ref.child("City").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(locality)){
                    ref.child("City").child(locality).setValue(locality);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void insertCountry(String countryName) {

        ref.child("Country").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(countryName)){
                    ref.child("Country").child(countryName).setValue(countryName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayLocation(String petID) {
        ref.child("Approved_req").child(petID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pet_Model pet_model = snapshot.getValue(Pet_Model.class);
                petMarker.setPosition(new LatLng(pet_model.getLatitude(),pet_model.getLongitude()));
                moveCamera((new LatLng(pet_model.getLatitude(),pet_model.getLongitude())),DEFAULT_ZOOM);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        petMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(82,135)));
        petMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pet_marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }

    public void moveCamera(LatLng latLng , float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }


    public void send_for_approvel(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toPath.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Approval Failed", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Approved", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ref.child("Approved_req").child(petID).removeValue();
                reference.child("Users").child(firebaseUser.getUid()).child("MyPets").child(petID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}