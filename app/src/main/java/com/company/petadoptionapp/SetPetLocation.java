package com.company.petadoptionapp;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.company.petadoptionapp.databinding.ActivitySetPetLocationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SetPetLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySetPetLocationBinding binding;
    private SearchView svMap;
    private final float DEFAULT_ZOOM = 15f;
    private Marker petMarker;
    private Address address;
    private Button btnSubmitLocation;
    private Boolean searchClick = false;
    private List<Address> addressesList;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Approval_req");
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetPetLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        svMap = findViewById(R.id.svMap);
        btnSubmitLocation = (Button) findViewById(R.id.btnSubmitLocation);

        String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        String name = intent.getStringExtra("PetName");
        String age = intent.getStringExtra("PetAge");
        String breed = intent.getStringExtra("PetBreed");
        String about = intent.getStringExtra("PetAbout");
        String gender = intent.getStringExtra("PetGender");
        String type = intent.getStringExtra("PetType");
        String UID = FirebaseAuth.getInstance().getUid();

        String img = intent.getStringExtra("imageUri");
        Uri uriImage = Uri.parse(img);

        svMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = svMap.getQuery().toString();

                if (location != null || !location.equals("")) {
                    searchClick = true;
                    Geocoder geocoder = new Geocoder((SetPetLocation.this));
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
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        btnSubmitLocation.setOnClickListener(view -> {
            if(searchClick){
                insertData(name,age,breed,about,gender,type,UID,address,uriImage);
            }else{

                Toast.makeText(this, "Please Select a Location", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void insertData(String name, String age, String breed, String about, String gender, String type,
                            String UID, Address address, Uri imageUri) {

        Map<String,Object> map=new HashMap<>();

        map.put("PetName",name);
        map.put("PetAge",age);
        map.put("PetBreed",breed);
        map.put("PetAbout",about);
        map.put("PetGender",gender);
        map.put("PetType",type);
        map.put("PetUser",UID);
        map.put("Latitude",address.getLatitude());
        map.put("Longitude",address.getLongitude());
        map.put("City",address.getLocality());
        map.put("State",address.getAdminArea());
        map.put("Country",address.getCountryName());


        DatabaseReference newRef = reference.push();
        String key = newRef.getKey();
        newRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SetPetLocation.this, "Pet Added for Approval", Toast.LENGTH_SHORT).show();
                insertCountry(address.getCountryName());
                insertCity(address.getLocality());
                insertState(address.getAdminArea());
                startActivity(new Intent(SetPetLocation.this,MainActivity.class));
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetPetLocation.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        UUID randomID = UUID.randomUUID();
        String imageName = "images/"+randomID+".jpg";
        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference myStorageRef = storage.getReference(imageName);
                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference().child("Approval_req")
                                .child(key).child("ImageUrl").setValue(uri.toString());
                    }
                });
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
        LatLng India = new LatLng(20,79);
        petMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(82,135)));
        petMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pet_marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(India));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Add a marker in Sydney and move the camera
    }

    public void moveCamera(LatLng latLng , float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
}