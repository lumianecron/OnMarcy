package com.android.onmarcy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import model.Campaign;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    Activity activity;
    private RecyclerView rvHistory;
    private CampaignAdapter campaignAdapter;
    private ArrayList<Campaign> campaigns = new ArrayList<>();

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        rvHistory = view.findViewById(R.id.rv_history);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        campaignAdapter = new CampaignAdapter(campaigns);
        campaignAdapter.setOnItemCallback(new CampaignAdapter.OnItemCallback() {
            @Override
            public void onItemClicked(Campaign campaign) {
                Toast.makeText(activity, campaign.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showContent(Campaign campaign) {
                Intent intent = new Intent(activity, ContentActivity.class);
                intent.putExtra(ContentActivity.EXTRA_CAMPAIGN, campaign);
                startActivity(intent);
            }

            @Override
            public void update(Campaign campaign) {
                Toast.makeText(activity, "Update", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void delete(Campaign campaign) {
                Toast.makeText(activity, "Delete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showResult(Campaign campaign) {
                Toast.makeText(activity, "Show Result", Toast.LENGTH_SHORT).show();
            }
        });
        rvHistory.setAdapter(campaignAdapter);
        getCampaign();
    }

    private void getCampaign(){

        //select campaign
        for (int i = 4; i < 7; i++) {
            Campaign.select(getActivity(), "", 0, "", "", "", 0, "", "", "", 0
                    , 0, 0, 0, 0, i, "", "", 0, 10, new Campaign.CallbackSelect() {
                @Override
                public void success(JSONArray data) {
                    for (int j = 0; j < data.length(); j++) {
                        try {
                            campaigns.add(new Campaign(data.getJSONObject(j)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    campaignAdapter.notifyDataSetChanged();
                }

                @Override
                public void error() {
                    Toast.makeText(activity, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}