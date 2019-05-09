package com.example.firebaseloginauth.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.firebaseloginauth.Adapter.CustomCoffeeAdapter;
import com.example.firebaseloginauth.DTO.CoffeeDTO;
import com.example.firebaseloginauth.R;
import com.example.firebaseloginauth.map;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class CoffeeFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://fir-loginauth-3808b.appspot.com");
    private FirebaseAuth mAuth;
    ListView listView;
    List<CoffeeDTO> list_music;
    List<CoffeeDTO> list_music_new ;
    CustomCoffeeAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SearchView searchView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listcoffee,container,false);
        searchView = view.findViewById(R.id.searchviewCF);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("coffee");
        }
        listView = view.findViewById(R.id.listViewCF);

        list_music = new ArrayList<CoffeeDTO>();

        getDataInFireBase();

        context = getActivity().getApplicationContext();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CoffeeDTO itemMusic = (CoffeeDTO) parent.getItemAtPosition(position);

                Intent listIntent = new Intent(context, map.class);
                listIntent.putExtra("coffeeitem",itemMusic);
                getActivity().startActivity(listIntent);


            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!= null && !newText.isEmpty()){
                    getchuoi(list_music,newText.toLowerCase());
                }else{
                    adapter = new CustomCoffeeAdapter(context,R.layout.layout_custom_coffee,list_music);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });

        return  view;
    }

    public void getchuoi(List<CoffeeDTO> list_music1,String stringS){
        stringS.toLowerCase();
        list_music_new = new ArrayList<CoffeeDTO>();
        for (CoffeeDTO i : list_music1){
            if(i.getIfCoffee().toLowerCase().trim().indexOf(stringS)>=0){
                list_music_new.add(i);
            }
        }
        adapter = new CustomCoffeeAdapter(getActivity().getApplicationContext(),R.layout.layout_custom_coffee,list_music_new);
        listView.setAdapter(adapter);
    }

    public void getDataInFireBase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getChildrenCount() == 0){
                    adapter = new CustomCoffeeAdapter(context,R.layout.layout_custom_coffee,list_music);
                    listView.setAdapter(adapter);
                }else{
                    for(DataSnapshot dataS : dataSnapshot.getChildren()){
                        CoffeeDTO musicDTO1 = new CoffeeDTO();
                        musicDTO1 = dataS.getValue(CoffeeDTO.class);
                        list_music.add(musicDTO1);
                    }
                    adapter = new CustomCoffeeAdapter(context,R.layout.layout_custom_coffee,list_music);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Data    ", "Failed to read value.", error.toException());
            }
        });
    }
}
