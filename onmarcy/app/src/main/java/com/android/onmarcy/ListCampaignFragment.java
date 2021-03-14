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

import java.util.ArrayList;

import model.Campaign;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListCampaignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListCampaignFragment extends Fragment {
    Activity activity;
    RecyclerView rvCampaign;
    private ArrayList<Campaign> campaigns = new ArrayList<>();

    public ListCampaignFragment() {
        // Required empty public constructor
    }

    public static ListCampaignFragment newInstance() {
        ListCampaignFragment fragment = new ListCampaignFragment();
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
        return inflater.inflate(R.layout.fragment_list_campaign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        rvCampaign = view.findViewById(R.id.rv_campaign);
        rvCampaign.setHasFixedSize(true);
        rvCampaign.setLayoutManager(new LinearLayoutManager(getContext()));
        getCampaign();
        CampaignAdapter campaignAdapter = new CampaignAdapter(campaigns);
        rvCampaign.setAdapter(campaignAdapter);
    }

    private void getCampaign(){
        //select campaign
    }
}