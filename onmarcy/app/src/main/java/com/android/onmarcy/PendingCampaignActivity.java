package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import model.Campaign;

public class PendingCampaignActivity extends AppCompatActivity {
    private RecyclerView rvPendingCampaign;
    private ArrayList<Campaign> campaigns = new ArrayList<>();
    private PendingCampaignAdapter pendingCampaignAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_campaign);
        getSupportActionBar().setTitle(R.string.pending_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPendingCampaigns(){
        Campaign.select(this, "", 0, "", "", "", 0, "", "", "", 0
                , 0, 0, 0, 0, 4, "", "", 0, 10, 0, new Campaign.CallbackSelect() {
                    @Override
                    public void success(JSONArray data) {
                        campaigns.clear();
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                campaigns.add(new Campaign(data.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        pendingCampaignAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error() {
                        Toast.makeText(PendingCampaignActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    }
        });
    }
}