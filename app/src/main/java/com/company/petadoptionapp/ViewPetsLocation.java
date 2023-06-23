package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.company.petadoptionapp.databinding.ActivityViewPetsLocationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewPetsLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityViewPetsLocationBinding binding;
    private Marker petMarker;
    private final float DEFAULT_ZOOM = 15f;
    private Button btnGoogleMaps;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewPetsLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnGoogleMaps = findViewById(R.id.btnGoogleMaps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        String petID = i.getStringExtra("view_pet_id");

        DatabaseReference petReference = FirebaseDatabase.getInstance().getReference().child("Approved_req");
        petReference.child(petID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pet_Model pet_model = snapshot.getValue(Pet_Model.class);
                address = pet_model.getLatitude()+","+pet_model.getLongitude();
                petMarker.setPosition(new LatLng(pet_model.getLatitude(),pet_model.getLongitude()));
                moveCamera((new LatLng(pet_model.getLatitude(),pet_model.getLongitude())),DEFAULT_ZOOM);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnGoogleMaps.setOnClickListener(view -> {

            if(address != null || address.equals("")){
                openMap(address);
            }else{
                Toast.makeText(this, "ERROR : Location is empty", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void openMap(String address) {
        Uri uri = Uri.parse("geo:0, 0?q="+address);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
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
        LatLng India = new LatLng(20,79);
        mMap.addMarker(new MarkerOptions().position(India).title("Marker in Sydney"));
        petMarker = mMap.addMarker(new MarkerOptions().position(India));
        petMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pet_marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(India));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }

    public void moveCamera(LatLng latLng , float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
}