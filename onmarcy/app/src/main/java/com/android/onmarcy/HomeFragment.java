package com.android.onmarcy;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import model.Campaign;
import model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Activity activity;
    RecyclerView rvCampaign;
    FloatingActionButton floatingActionButton;
    private ArrayList<Campaign> campaigns = new ArrayList<>();
    private ArrayList<Campaign> temp = new ArrayList<>();
    private CampaignAdapter campaignAdapter;
    private User user;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();

        try {
            user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.floating_action_button);
        rvCampaign = view.findViewById(R.id.rv_campaign);
        rvCampaign.setHasFixedSize(true);
        rvCampaign.setLayoutManager(new LinearLayoutManager(getContext()));
        getFilteredCampaign(1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDialogFragment.display(getParentFragmentManager());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getString(R.string.search_campaign));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    temp.clear();
                    for (int i = 0; i < campaigns.size(); i++) {
                        if (campaigns.get(i).getTitle().toUpperCase().contains(s.toUpperCase())) {
                            temp.add(campaigns.get(i));
                        }
                    }
                    campaignAdapter.notifyDataSetChanged();
                    rvCampaign.setAdapter(campaignAdapter);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_filter:
                FilterDialog filterDialog = new FilterDialog(activity, user.getUserType());
                Window window = filterDialog.getWindow();
                WindowManager.LayoutParams param = window.getAttributes();
                param.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                param.y = 110;
                window.setAttributes(param);
                filterDialog.onMyDialogResult = new FilterDialog.OnMyDialogResult() {
                    @Override
                    public void finish(int status, int lowest, int highest, int year) {
                        getFilteredCampaign(status, lowest, highest, year);
                    }
                };
                filterDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFilteredCampaign(int status, int lowest, int highest, int year) {
        Campaign.select(getActivity(), "", status, "", 0, 0, 0, 10, new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                temp.clear();
                campaigns.clear();
                for (int i = 0; i < data.length(); i++) {
                    try {
                        campaigns.add(new Campaign(data.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (year != 0) {
                    for (int i = 0; i < campaigns.size(); i++) {
                        int yearTemp = Integer.parseInt(campaigns.get(i).getDate().split("-")[0]);
                        if (campaigns.get(i).getPrice() >= lowest && campaigns.get(i).getPrice() <= highest && yearTemp == year) {
                            temp.add(campaigns.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < campaigns.size(); i++) {
                        if (campaigns.get(i).getPrice() >= lowest && campaigns.get(i).getPrice() <= highest) {
                            temp.add(campaigns.get(i));
                        }
                    }
                }
                campaignAdapter = new CampaignAdapter(temp);
                campaignAdapter.setOnItemCallback(new CampaignAdapter.OnItemCallback() {
                    @Override
                    public void onItemClicked(Campaign campaign) {
                        Toast.makeText(activity, campaign.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
                rvCampaign.setAdapter(campaignAdapter);
            }

            @Override
            public void error() {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}