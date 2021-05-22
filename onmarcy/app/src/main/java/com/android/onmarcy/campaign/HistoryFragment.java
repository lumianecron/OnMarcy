package com.android.onmarcy.campaign;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.Global;
import com.android.onmarcy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import model.Campaign;
import model.User;

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
    private TextView tvNotFound;
    private User user;

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

        try {
            user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvNotFound = view.findViewById(R.id.tv_not_found);
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
                Intent intent = new Intent(activity, ViewResultActivity.class);
                intent.putExtra(ViewResultActivity.EXTRA_CAMPAIGN, campaign);
                startActivity(intent);
            }
        });
        rvHistory.setAdapter(campaignAdapter);

        if(user.getUserType() == 1){ //Brand
            getCampaignBrand();
        }
        if(user.getUserType() == 2){ //Marketer
            getCampaignMarketer();
        }
    }

    private void getCampaignBrand(){
        //select campaign
        for (int i = 5; i < 7; i++) {
            Campaign.select(activity, "", 0, "", "", "", 0, "", "", "", 0
                    , 0, 0, 0, 0, i, "", "", 0, 10, 0, new Campaign.CallbackSelect() {
                @Override
                public void success(JSONArray data) {
                    for (int j = 0; j < data.length(); j++) {
                        try {
                            campaigns.add(new Campaign(data.getJSONObject(j)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    filter();

                    if(campaigns.size() == 0){
                        tvNotFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void error() {
                    Toast.makeText(activity, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getCampaignMarketer(){
        Campaign.selectCampaignResult(activity, new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                for (int j = 0; j < data.length(); j++) {
                    try {
                        campaigns.add(new Campaign(data.getJSONObject(j)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                filter();

                if(campaigns.size() == 0){
                    tvNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error() {
                Toast.makeText(activity, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < campaigns.size(); i++) {
            try {
                calendar2.setTime(simpleDateFormat.parse(campaigns.get(i).getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(calendar1.get(Calendar.MONTH) + 1 != calendar2.get(Calendar.MONTH) + 1 || calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)){
                campaigns.remove(i);
            }
        }
        campaignAdapter.notifyDataSetChanged();
    }
}