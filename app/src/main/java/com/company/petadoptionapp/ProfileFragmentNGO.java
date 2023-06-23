package com.company.petadoptionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragmentNGO extends Fragment {

    public ProfileFragmentNGO() {
        // Required empty public constructor
    }

    private TextView tvNameNGO, tvRegNoNGO, tvMobileNGO, tvEmailNGO;
    private CardView cvMyPetsNGO, cvLogoutNGO;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_n_g_o, container, false);

        tvNameNGO = view.findViewById(R.id.tvProfileNameNGO);
        tvRegNoNGO = view.findViewById(R.id.tvProfileRegNo);
        tvMobileNGO = view.findViewById(R.id.tvProfilePhoneNGO);
        tvEmailNGO = view.findViewById(R.id.tvProfileEmailNGO);
        cvLogoutNGO = view.findViewById(R.id.cvLogoutNGO);
        cvMyPetsNGO = view.findViewById(R.id.cvManagePetsNGO);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();

        String uidNGO = auth.getUid();

        if(uidNGO != null) {

            reference.child("NGO").child(uidNGO).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NGO_Model ngo = snapshot.getValue(NGO_Model.class);
                    setProfileData(ngo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        cvLogoutNGO.setOnClickListener(view1 -> {
            auth.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(),LoginActivity.class));
            getActivity().finish();
        });

        cvMyPetsNGO.setOnClickListener(view1 -> {
            Intent i = new Intent(getContext(),ManageLostPets.class);
            startActivity(i);
        });

        return view;
    }

    private void setProfileData(NGO_Model ngo) {

        tvNameNGO.setText(ngo.ngoName);
        tvEmailNGO.setText(ngo.ngoEmail);
        tvMobileNGO.setText(ngo.ngoPhone);
        tvRegNoNGO.setText(ngo.ngoRegNo);
    }
}