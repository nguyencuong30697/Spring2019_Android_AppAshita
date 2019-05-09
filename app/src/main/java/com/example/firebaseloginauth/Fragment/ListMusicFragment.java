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

import com.example.firebaseloginauth.Adapter.CustomMusicAdapter;
import com.example.firebaseloginauth.DTO.MusicDTO;
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

import static com.example.firebaseloginauth.BottomController.navigation;
import static com.example.firebaseloginauth.BottomController.numberOfPllayMusic;
import static com.example.firebaseloginauth.Fragment.PlayyMusicAppFragment.stopMusic;

public class ListMusicFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://fir-loginauth-3808b.appspot.com");
    private FirebaseAuth mAuth;
    ListView listView;
    List<MusicDTO> list_music;
    List<MusicDTO> list_music_new ;
    CustomMusicAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static MusicDTO musicDTO;
    SearchView searchView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listmusic,container,false);
        searchView = view.findViewById(R.id.searchview);

        context = getActivity().getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("music");
        }
        listView = view.findViewById(R.id.listViewMusic);
        list_music = new ArrayList<MusicDTO>();
        getDataInFireBase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicDTO itemMusic = (MusicDTO) parent.getItemAtPosition(position);
                int positionMusic = 0;
                for(int i=0;i<list_music.size();i++){
                    if(list_music.get(i).getIdMusic().equalsIgnoreCase(itemMusic.getIdMusic())){
                        positionMusic = i;
                    }
                }
                Bundle b = new Bundle();
                b.putInt("position",positionMusic);
                stopMusic = false;
                navigation.setSelectedItemId(numberOfPllayMusic);
                PlayyMusicAppFragment fragment2 = new PlayyMusicAppFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment2.setArguments(b);
                fragmentTransaction.replace(R.id.frame_thay_the, fragment2);
                fragmentTransaction.commit();


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
                    adapter = new CustomMusicAdapter(context,R.layout.layout_customlist,list_music);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });


        return  view;
    }

    public void getchuoi(List<MusicDTO> list_music1,String stringS){
        stringS.toLowerCase();
        list_music_new = new ArrayList<MusicDTO>();
        for (MusicDTO i : list_music1){
            if(i.getDisplayname().toLowerCase().trim().indexOf(stringS)>=0){
                list_music_new.add(i);
            }
        }
        adapter = new CustomMusicAdapter(context,R.layout.layout_customlist,list_music_new);
        listView.setAdapter(adapter);
    }

    public void getDataInFireBase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getChildrenCount() == 0){
                    adapter = new CustomMusicAdapter(context,R.layout.layout_customlist,list_music);
                    listView.setAdapter(adapter);
                }else{
                    for(DataSnapshot dataS : dataSnapshot.getChildren()){
                        MusicDTO musicDTO1 = new MusicDTO();
                        musicDTO1 = dataS.getValue(MusicDTO.class);
                        list_music.add(musicDTO1);
                    }
                    adapter = new CustomMusicAdapter(context,R.layout.layout_customlist,list_music);
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
