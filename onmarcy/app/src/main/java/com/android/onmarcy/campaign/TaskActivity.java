package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.HomeActivity;
import com.android.onmarcy.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import model.Campaign;

public class TaskActivity extends AppCompatActivity {
    private ArrayList<Campaign> campaigns = new ArrayList<>();
    private ArrayList<Campaign> temp = new ArrayList<>();
    private RecyclerView rvCampaign;
    private TextView tvNotFound;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setTitle(R.string.your_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvCampaign = findViewById(R.id.rv_campaign);
        tvNotFound = findViewById(R.id.tv_not_found);
        rvCampaign.setHasFixedSize(true);
        rvCampaign.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(campaigns);
        rvCampaign.setAdapter(taskAdapter);

        taskAdapter.setOnItemCallBack(new TaskAdapter.OnItemCallBack() {
            @Override
            public void onItemClicked(Campaign campaign) {
                EndTaskDialogFragment.display((TaskActivity.this).getSupportFragmentManager(), campaign);
            }
        });

        getTask();
    }

    private void getTask() {
        campaigns.clear();
        Campaign.selectMarketerTask(this, new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        campaigns.add(new Campaign(data.getJSONObject(i)));
                        taskAdapter.notifyDataSetChanged();

                        setVisibility();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error() {
                setVisibility();
            }
        });
    }

    private void setVisibility(){
        if (campaigns.size() == 0) {
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