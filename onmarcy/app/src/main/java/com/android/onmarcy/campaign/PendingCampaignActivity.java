package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import model.Campaign;

public class PendingCampaignActivity extends AppCompatActivity {
    private RecyclerView rvPendingCampaign;
    private ArrayList<Campaign> campaigns = new ArrayList<>();
    private PendingCampaignAdapter pendingCampaignAdapter;
    private TextView tvNotFound;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_campaign);
        getSupportActionBar().setTitle(R.string.pending_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        tvNotFound = findViewById(R.id.tv_not_found);
        rvPendingCampaign = findViewById(R.id.rv_pending_campaign);
        rvPendingCampaign.setHasFixedSize(true);
        rvPendingCampaign.setLayoutManager(new LinearLayoutManager(this));
        pendingCampaignAdapter = new PendingCampaignAdapter(campaigns);
        rvPendingCampaign.setAdapter(pendingCampaignAdapter);

        pendingCampaignAdapter.setOnItemCallBack(new PendingCampaignAdapter.OnItemCallBack() {
            @Override
            public void onLinkClicked(Campaign campaign) {
                WebDialogFragment.display((PendingCampaignActivity.this).getSupportFragmentManager(), campaign.getLink());
            }
        });

        getPendingCampaigns();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPendingCampaigns() {
        Campaign.selectPending(this, new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                progressBar.setVisibility(View.GONE);
                campaigns.clear();

                for (int i = 0; i < data.length(); i++) {
                    try {
                        campaigns.add(new Campaign(data.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pendingCampaignAdapter.notifyDataSetChanged();

                if (campaigns.size() < 1) {
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    tvNotFound.setVisibility(View.GONE);
                }
            }

            @Override
            public void error() {
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
            }
        });
    }
}