package com.android.onmarcy;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.onmarcy.campaign.HistoryFragment;
import com.android.onmarcy.campaign.HomeFragment;
import com.android.onmarcy.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import model.User;

public class HomeActivity extends AppCompatActivity {
    public static String TAG = "action_activity_to_profile_fragment";
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this).setCancelable(false);
                alert.setMessage(R.string.msg_back);

                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragment = HomeFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                        getSupportActionBar().setTitle(R.string.app_name);
                        return true;
                    case R.id.menu_profile:
                        fragment = ProfileFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                        getSupportActionBar().setTitle(R.string.profile);
                        return true;
                    case R.id.menu_history:
                        fragment = HistoryFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                        getSupportActionBar().setTitle(R.string.history);
                        return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            fragment = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
            getSupportActionBar().setTitle(R.string.app_name);
        }

        if (getIntent().hasExtra(TAG)) {
            bottomNavigationView.setSelectedItemId(R.id.menu_profile);
        }
    }

    public void profileFragment() {
        fragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        getSupportActionBar().setTitle(R.string.profile);
    }
}