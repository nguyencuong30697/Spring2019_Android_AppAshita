package com.example.firebaseloginauth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.firebaseloginauth.Fragment.CoffeeFragment;
import com.example.firebaseloginauth.Fragment.ListMusicFragment;
import com.example.firebaseloginauth.Fragment.PlayyMusicAppFragment;

import static com.example.firebaseloginauth.Fragment.PlayyMusicAppFragment.stopMusic;

public class BottomController extends AppCompatActivity {

    FragmentManager fragmentManager;
    public static BottomNavigationView navigation;
    public static int numberOfPllayMusic;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    stopMusic = false;
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    ListMusicFragment a1 = new ListMusicFragment();
                    fragmentTransaction2.replace(R.id.frame_thay_the,a1);
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_dashboard:
                    stopMusic = true;
                    FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                    PlayyMusicAppFragment a = new PlayyMusicAppFragment();
                    fragmentTransaction1.replace(R.id.frame_thay_the,a);
                    fragmentTransaction1.commit();
                    return true;
                case R.id.navigation_coffee:
                    stopMusic = false;
                    FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                    CoffeeFragment a3 = new CoffeeFragment();
                    fragmentTransaction3.replace(R.id.frame_thay_the,a3);
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_controller);
        numberOfPllayMusic = R.id.navigation_dashboard;
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager =  getSupportFragmentManager();

        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
        ListMusicFragment a1 = new ListMusicFragment();
        fragmentTransaction2.replace(R.id.frame_thay_the,a1);
        fragmentTransaction2.commit();

    }

    @Override
    public void onBackPressed() {
        stopMusic = false;
        super.onBackPressed();
    }
}
