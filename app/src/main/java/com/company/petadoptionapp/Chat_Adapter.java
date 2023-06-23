package com.company.petadoptionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.ChatViewHolder> {

    List<Chat_Model> list;
    String userName;
    boolean status;
    int send;
    int recive;

    public Chat_Adapter(List<Chat_Model> list, String userName) {
        this.list = list;
        this.userName = userName;

        status = false;
        send = 1;
        recive = 2;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == send)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_card,parent,false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_card,parent,false);
        }

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            if(status)
            {
                textView = itemView.findViewById(R.id.tvSend);
            }
            else
            {
                textView = itemView.findViewById(R.id.tvReceived);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {

        if(list.get(position).getFrom().equals(userName))
        {
            status = true;
            return send;
        }
        else
        {
            status = false;
            return recive;
        }
    }
}
