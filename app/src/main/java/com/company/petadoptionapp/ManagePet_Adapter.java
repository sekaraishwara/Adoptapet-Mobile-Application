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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ManagePet_Adapter extends RecyclerView.Adapter<ManagePet_Adapter.ManagePetViewHolder>{

    List<String> pets;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public ManagePet_Adapter(List<String> pets, Context context) {
        this.pets = pets;
        this.context = context;

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public ManagePetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_card_view,parent,false);

        return new ManagePetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePetViewHolder holder, int position) {
        reference.child("Approved_req").child(pets.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String PetImage = snapshot.child("ImageUrl").getValue().toString();
                String PetName = snapshot.child("PetName").getValue().toString();
                String PetBreed = snapshot.child("PetBreed").getValue().toString();

                holder.petName.setText(PetName);
                holder.petBreed.setText(PetBreed);

                Picasso.get().load(PetImage).into(holder.imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String id = pets.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.cvManagePets.getContext(), EditPets.class);
                i.putExtra("petID",id);
                holder.cvManagePets.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }


    public class ManagePetViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView petName,petBreed;
        CardView cvManagePets;

        public ManagePetViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivShowPet);
            petName = itemView.findViewById(R.id.tvPetName);
            petBreed = itemView.findViewById(R.id.tvPetBreed);
            cvManagePets = itemView.findViewById(R.id.petview);
        }
    }
}
