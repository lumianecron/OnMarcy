package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.HomeActivity;
import com.android.onmarcy.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import model.Approach;
import model.Campaign;
import model.User;

public class ViewMarketerActivity extends AppCompatActivity {
    public static final String EXTRA_CODE = "code";
    private RecyclerView rvMarketer;
    private TextView tvNotFound;
    private MarketerAdapter marketerAdapter;
    private ArrayList<User> users = new ArrayList<>();
    private String date = "";
    private int code = 0;
    private ArrayList<Approach> approaches = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_marketer);
        getSupportActionBar().setTitle(R.string.list_marketer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        rvMarketer = findViewById(R.id.rv_marketer);
        tvNotFound = findViewById(R.id.tv_not_found);
        rvMarketer.setHasFixedSize(true);
        rvMarketer.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra(EXTRA_CODE)) {
            code = getIntent().getIntExtra(EXTRA_CODE, 0);
            marketerAdapter = new MarketerAdapter(users, approaches, code);
        }

        rvMarketer.setAdapter(marketerAdapter);
        marketerAdapter.setOnItemCallback(new MarketerAdapter.OnItemCallback() {
            @Override
            public void onItemClicked(User user) {
                Intent intent = new Intent(ViewMarketerActivity.this, DetailMarketerActivity.class);
                intent.putExtra(DetailMarketerActivity.EXTRA_USERNAME, user.getUsername());
                intent.putExtra(DetailMarketerActivity.EXTRA_NAME, user.getName());
                intent.putExtra(DetailMarketerActivity.EXTRA_PHOTO, user.getPhotoUrl());
                startActivity(intent);
            }

            @Override
            public void accept(String username, int code) {
                Campaign.chooseMarketer(ViewMarketerActivity.this, code, username, false, new Campaign.Callback() {
                    @Override
                    public void success() {
                        Toast.makeText(ViewMarketerActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ViewMarketerActivity.this, HomeActivity.class));
                        finishAffinity();
                    }

                    @Override
                    public void error() {
                        Toast.makeText(ViewMarketerActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        getMarketer();
    }

    private void getMarketer() {
        Campaign.selectUserApproach(this, code, new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                progressBar.setVisibility(View.GONE);
                for (int i = 0; i < data.length(); i++) {
                    try {
                        User user = new User(data.getJSONObject(i));
                        Approach approach = new Approach(data.getJSONObject(i));
                        users.add(user);
                        approaches.add(approach);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                marketerAdapter.notifyDataSetChanged();

                setVisibility();
            }

            @Override
            public void error() {
                setVisibility();
            }
        });
    }

    private void setVisibility() {
        if (users.size() < 1) {
            tvNotFound.setVisibility(View.VISIBLE);
        } else {
            tvNotFound.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}