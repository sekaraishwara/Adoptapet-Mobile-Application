package com.company.petadoptionapp;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    Pet_Adapter mainAdapter;
    FirebaseDatabase database;
    DatabaseReference ref, city, country, state;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.rv_list);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Pet_Model> options =
                new FirebaseRecyclerOptions.Builder<Pet_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Approved_req"), Pet_Model.class)
                        .build();

        mainAdapter= new Pet_Adapter(options);
        recyclerView.setAdapter(mainAdapter);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        city = ref.child("City");
        state = ref.child("State");
        country = ref.child("Country");

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
                checkSearch(s,true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                checkSearch(s,false);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void checkSearch(String s, boolean check) {
        city.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(s)){
                    searchCity(s);
                }else{
                    country.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(s)){
                                searchCountry(s);
                            }else{
                                state.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(s)){
                                            searchState(s);
                                        }else{
                                            if(check == true) {
                                                Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void searchCity(String str){
        FirebaseRecyclerOptions<Pet_Model> options =
                new FirebaseRecyclerOptions.Builder<Pet_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Approved_req").orderByChild("City").startAt(str).endAt(str+"~"), Pet_Model.class)
                        .build();
        mainAdapter = new Pet_Adapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    private void searchState(String str){
        FirebaseRecyclerOptions<Pet_Model> options =
                new FirebaseRecyclerOptions.Builder<Pet_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Approved_req").orderByChild("State").startAt(str).endAt(str+"~"), Pet_Model.class)
                        .build();
        mainAdapter = new Pet_Adapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    private void searchCountry(String str){
        FirebaseRecyclerOptions<Pet_Model> options =
                new FirebaseRecyclerOptions.Builder<Pet_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Approved_req").orderByChild("Country").startAt(str).endAt(str+"~"), Pet_Model.class)
                        .build();
        mainAdapter = new Pet_Adapter(options);
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
