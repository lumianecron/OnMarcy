package com.android.onmarcy.campaign;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.Global;
import com.android.onmarcy.HomeActivity;
import com.android.onmarcy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import model.Campaign;
import model.SocialMedia;
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
    private TextView tvNotFound;

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
        tvNotFound = view.findViewById(R.id.tv_not_found);
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

        if (user.getUserType() == 1) {
            checkStatus();
        }

        System.out.println(getIntervalDateTime("2020-07-23"));
    }

    private String getIntervalDateTime(String date){
        String[] arrDate = date.split("-");
        String myString = getString(R.string.date, arrDate[2], arrDate[1], arrDate[0]) + " " + "08:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date datetime;
        Date currentDatetime;
        String text = "";

        try {
            datetime = simpleDateFormat.parse(myString);

            DateFormat mFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            String currentTime = mFormat.format(calendar.getTime());
            currentDatetime = mFormat.parse(currentTime);

            long diff = currentDatetime.getTime() - datetime.getTime();
            long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
            long diffDays = TimeUnit.MILLISECONDS.toDays(diff);
            long diffWeeks = diff / (7 * 24 * 60 * 60 * 1000);
            long diffMonths = (diff / (7 * 24 * 60 * 60 * 1000) / 4);
            long diffYears = diff / (7 * 24 * 60 * 60 * 1000) / (4 * 12);

            boolean isMinute = false;
            boolean isHour = false;
            boolean isDay = false;
            boolean isWeek = false;
            boolean isMonth = false;
            boolean isYear = false;

            if (diffSeconds < 60) {
                text = diffSeconds + " seconds ago";
            } else if (diffSeconds > 60) {
                isMinute = true;
            }

            if (diffMinutes < 2 && isMinute) {
                text = diffMinutes + " minute ago";
            } else if (diffMinutes > 1 && diffMinutes < 60 && isMinute) {
                text = diffMinutes + " minutes ago";
            } else if (diffMinutes > 59) {
                isHour = true;
            }

            if (diffHours < 2 && isHour) {
                text = diffHours + " hour ago";
            } else if (diffHours > 1 && diffHours < 24 && isHour) {
                text = diffHours + " hours ago";
            } else if (diffHours > 23) {
                isDay = true;
            }

            if (diffDays < 2 && isDay) {
                text = diffDays + " day ago";
            } else if (diffDays > 1 && diffDays < 7 && isDay) {
                text = diffDays + " days ago";
            } else if (diffDays > 6 && isDay) {
                isWeek = true;
            }

            if (diffWeeks < 2 && isWeek) {
                text = diffWeeks + " week ago";
            } else if (diffWeeks > 1 && diffWeeks < 4 && isWeek) {
                text = diffWeeks + " weeks ago";
            } else if (diffWeeks > 3 && isWeek) {
                isMonth = true;
            }

            if (diffMonths < 2 && isMonth) {
                text = diffMonths + " month ago";
            } else if (diffMonths > 1 && diffMonths < 12 && isMonth) {
                text = diffMonths + " months ago";
            } else if (diffMonths > 11 && isMonth) {
                isYear = true;
            }

            if (diffYears < 2 && isYear) {
                text = diffYears + " year ago";
            } else if (diffYears > 1 && isYear) {
                text = diffYears + " years ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return text;
    }

    private void checkStatus() {
        SocialMedia.select(getActivity(), new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    SocialMedia socialMedia = new SocialMedia(jsonObject);

                    if (socialMedia.getStatusVerify() == 0) {
                        floatingActionButton.setVisibility(View.GONE);
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    Log.d("RUNNN", ex + "");
                }
            }

            @Override
            public void error() {
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

                    if (temp.size() < 1) {
                        tvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        tvNotFound.setVisibility(View.GONE);
                    }

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
        Campaign.select(getActivity(), "", 0, "", "", "", 0, "", "", "", 0
                , 0, 0, 0, 0, status, "", "", 0, 10, 0, new Campaign.CallbackSelect() {
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

                        if (temp.size() < 1) {
                            tvNotFound.setVisibility(View.VISIBLE);
                        } else {
                            tvNotFound.setVisibility(View.GONE);
                        }

                        campaignAdapter.setOnItemCallback(new CampaignAdapter.OnItemCallback() {
                            @Override
                            public void onItemClicked(Campaign campaign) {
                                Toast.makeText(activity, campaign.getTitle(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void showContent(Campaign campaign) {
                                HomeActivity homeActivity = (HomeActivity) getActivity();
                                Intent intent = new Intent(homeActivity, ContentActivity.class);
                                intent.putExtra(ContentActivity.EXTRA_CAMPAIGN, campaign);
                                startActivity(intent);
                            }

                            @Override
                            public void update(Campaign campaign) {
                                HomeActivity homeActivity = (HomeActivity) getActivity();
                                Intent intent = new Intent(homeActivity, UpdateActivity.class);
                                intent.putExtra(UpdateActivity.EXTRA_CAMPAIGN, campaign);
                                startActivity(intent);
                            }

                            @Override
                            public void delete(Campaign campaign) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setCancelable(false);
                                builder.setTitle(R.string.confirmation);
                                builder.setMessage(R.string.msg_delete_campaign);
                                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Campaign.delete(activity, campaign.getCodeString(), true, new Campaign.Callback() {
                                            @Override
                                            public void success() {
                                                getFilteredCampaign(1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
                                                Toast.makeText(activity, "Delete successful", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void error() {
                                                Toast.makeText(activity, "Delete unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            @Override
                            public void showResult(Campaign campaign) {
                                Toast.makeText(activity, "Show Result", Toast.LENGTH_SHORT).show();
                            }
                        });
                        rvCampaign.setAdapter(campaignAdapter);
                    }

                    @Override
                    public void error() {
                        Toast.makeText(activity, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}