package com.company.petadoptionapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragmentNGO extends Fragment {
    RecyclerView recyclerView;
    Lost_Pet_Adapter mainAdapter;
    FirebaseDatabase database;
    DatabaseReference ref;

    public HomeFragmentNGO() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_n_g_o, container, false);
        setHasOptionsMenu(true);
        recyclerView=view.findViewById(R.id.rv_lost_list_NGO);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Pet_Model> options =
                new FirebaseRecyclerOptions.Builder<Pet_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Lost_Approved_req"), Pet_Model.class)
                        .build();

        mainAdapter= new Lost_Pet_Adapter(options);
        recyclerView.setAdapter(mainAdapter);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.searchAction);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                s = s.substring(0, 1).toUpperCase() + s.substring(1);
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void search(String s) {
        FirebaseRecyclerOptions<Pet_Model> options =
                new FirebaseRecyclerOptions.Builder<Pet_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Lost_Approved_req").orderByChild("PetAddress").startAt(s).endAt(s+"~"), Pet_Model.class)
                        .build();
        mainAdapter = new Lost_Pet_Adapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
    }
