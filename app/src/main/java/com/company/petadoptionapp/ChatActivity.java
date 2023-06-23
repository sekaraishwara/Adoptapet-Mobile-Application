package com.company.petadoptionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String othername,otherUserImage,username;
    String otherId,userId;
    CircleImageView chatImage;
    TextView chatName;
    private RecyclerView rv;
    private EditText message;
    private FloatingActionButton send;
    FirebaseDatabase database;
    DatabaseReference reference;
    Chat_Adapter adapter;
    List<Chat_Model> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(ChatActivity.this,R.color.black));

        othername = getIntent().getStringExtra("otherUsername");
        otherUserImage = getIntent().getStringExtra("otherImage");
        username = getIntent().getStringExtra("CurrentUser");
        otherId = getIntent().getStringExtra("otherId");
        userId = getIntent().getStringExtra("userId");

        rv = findViewById(R.id.rvChat);
        message = findViewById(R.id.etMessage);
        send = findViewById(R.id.fab);
        chatName = findViewById(R.id.chatUsername);
        chatImage = findViewById(R.id.chatImage);

        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        chatName.setText(othername);
        Picasso.get().load(otherUserImage).into(chatImage);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usermessage = message.getText().toString();

                if(!usermessage.equals(""))
                {
                    message.setText("");
                    sendMessage(usermessage);
                    reference.child("Users").child(userId).child("MyChats").child(otherId).child("Name").setValue(othername);
                    reference.child("Users").child(otherId).child("MyChats").child(userId).child("Name").setValue(username);
                }
            }
        });

        getMessage();


    }

    public void sendMessage(String message)
    {
        String key = reference.child("Message").child(username).child(othername).push().getKey();
        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("from",username);
        reference.child("Message").child(username).child(othername).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Message").child(othername).child(username).child(key).setValue(messageMap);
            }
        });
    }


    public void getMessage()
    {
        reference.child("Message").child(username).child(othername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat_Model modleClass = snapshot.getValue(Chat_Model.class);
                list.add(modleClass);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new Chat_Adapter(list,username);
        rv.setAdapter(adapter);
    }



}