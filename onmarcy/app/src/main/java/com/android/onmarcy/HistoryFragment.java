package com.android.onmarcy;

import android.app.Activity;
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
    RecyclerView rvHistory;
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
        getCampaign();
    }

    private void getCampaign(){
        //select campaign
        Campaign.select(getActivity(), "", 1, "", 0, 0, 0, 10, new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        campaigns.add(new Campaign(data.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CampaignAdapter campaignAdapter = new CampaignAdapter(campaigns);
                rvHistory.setAdapter(campaignAdapter);
            }

            @Override
            public void error() {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}