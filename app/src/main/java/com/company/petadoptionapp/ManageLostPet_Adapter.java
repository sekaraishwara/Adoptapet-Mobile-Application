package com.company.petadoptionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ManageLostPet_Adapter extends RecyclerView.Adapter<ManageLostPet_Adapter.MagageLostPetViewHolder> {

    List<String> pets;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public ManageLostPet_Adapter(List<String> pets, Context context) {
        this.pets = pets;
        this.context = context;

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public MagageLostPetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_card_view,parent,false);

        return new MagageLostPetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MagageLostPetViewHolder holder, int position) {
        reference.child("Lost_Approved_req").child(pets.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String PetImage = snapshot.child("ImageUrl").getValue().toString();
                String PetGender = snapshot.child("PetGender").getValue().toString();
                String PetBreed = snapshot.child("PetBreed").getValue().toString();

                Picasso.get().load(PetImage).into(holder.imageView);
                holder.petGender.setText(PetGender);
                holder.petBreed.setText(PetBreed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String id = pets.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,EditLostPets.class);
                i.putExtra("petID",id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class MagageLostPetViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView petGender,petBreed;
        CardView cardView;
        public MagageLostPetViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivShowPet);
            petGender = itemView.findViewById(R.id.tvPetName);
            petBreed = itemView.findViewById(R.id.tvPetBreed);

            cardView = itemView.findViewById(R.id.petview);
        }
    }
}
