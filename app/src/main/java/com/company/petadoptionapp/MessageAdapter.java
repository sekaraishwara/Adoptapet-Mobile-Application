package com.company.petadoptionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    List<String> users;
    Context context;
    String userName;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    public MessageAdapter(List<String> users, Context context, String userName) {
        this.users = users;
        this.context = context;
        this.userName = userName;

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_card,parent,false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        int x = position;
        reference.child("Users").child(users.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherUsername = snapshot.child("userName").getValue().toString();
                String otherImage = snapshot.child("image").getValue().toString();

                holder.username.setText(otherUsername);
                Picasso.get().load(otherImage).into(holder.userimage);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context,ChatActivity.class);
                        i.putExtra("otherUsername",otherUsername);
                        i.putExtra("otherImage",otherImage);
                        i.putExtra("CurrentUser",userName);
                        i.putExtra("otherId",users.get(x));
                        i.putExtra("userId",firebaseUser.getUid());

                        context.startActivity(i);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private CircleImageView userimage;
        private CardView cardView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUserChat);
            userimage = itemView.findViewById(R.id.ivUserChat);
            cardView = itemView.findViewById(R.id.cvUserChat);
        }
    }
}
