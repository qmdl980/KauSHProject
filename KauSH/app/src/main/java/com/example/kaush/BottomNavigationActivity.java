package com.example.kaush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMusicList fragmentMusicList = new FragmentMusicList();
    private FragmentGraph1 fragmentGraph1 = new FragmentGraph1();
    private FragmentGraph2 fragmentGraph2 = new FragmentGraph2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMusicList).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.navigation_music_list:
                    transaction.replace(R.id.frameLayout, fragmentMusicList).commitAllowingStateLoss();

                    break;
                case R.id.navigation_graph1:
                    transaction.replace(R.id.frameLayout, fragmentGraph1).commitAllowingStateLoss();
                    break;
                case R.id.navigation_graph2:
                    transaction.replace(R.id.frameLayout, fragmentGraph2).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
