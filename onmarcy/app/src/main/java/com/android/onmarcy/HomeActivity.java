package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import model.User;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        try {
            user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        fragment = HomeFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                        getSupportActionBar().setTitle(R.string.app_name);
                        return true;
                    case R.id.menu_profile:
                        fragment = ProfileFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                        getSupportActionBar().setTitle(R.string.profile);
                        return true;
                    case R.id.menu_history:
                        fragment = HistoryFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                        getSupportActionBar().setTitle(R.string.history);
                        return true;
                }
                return false;
            }
        });

        if(savedInstanceState == null){
            fragment = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    void profileFragment(){
        fragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        getSupportActionBar().setTitle(R.string.profile);
    }
}