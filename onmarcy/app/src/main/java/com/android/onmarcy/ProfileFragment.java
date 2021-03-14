package com.android.onmarcy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.City;
import model.SocialMedia;
import model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Activity activity;

    CircleImageView imageView;
    TextInputEditText edtName, edtUsername, edtEmail, edtPhone, edtPassword, edtConfirm, edtVerification, edtInstagram;
    TextInputLayout textInputLayoutInstagram, textInputLayoutVerification;
    TextView tvStatus, tvFollower, tvCategory, tvFollowing, tvUsername, tvTotalPost, tvTotalComment, tvTotalLike, tvMinAge, tvMaxAge, tvMale, tvFemale, tvTimePosting, tvServiceType;
    SearchableSpinner spCity;
    Button btnSend, arrow, btnVerify;
    MenuItem menuAdd;
    LinearLayout hiddenView;
    CardView cardView;
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayAdapter<City> adapter;
    private User user;
    private int code = 0;
    private String cityName = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        bindData(view);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = getCode(spCity.getSelectedItem().toString());
                SocialMedia.insert(getActivity(), user.getUsername(), 1, 2, code, edtInstagram.getText().toString(), 10, 100, 50, 45, 120, 20, 35, 23, 52, "20:00", 1, 0, 0, "", true, new SocialMedia.Callback() {
                    @Override
                    public void success() {
                        Global.showLoading(getContext(), "success", "info");
                    }

                    @Override
                    public void error() {
                        Global.showLoading(getContext(), "error", "info");
                    }
                });
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (hiddenView.getVisibility() == View.VISIBLE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    }
                    hiddenView.setVisibility(View.GONE);
                    arrow.setText(R.string.expand);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);
                    arrow.setText(R.string.collapse);
                }
            }
        });
    }

    private void bindView(View view) {
        imageView = view.findViewById(R.id.image_view);
        edtName = view.findViewById(R.id.edt_name);
        edtUsername = view.findViewById(R.id.edt_username);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtPassword = view.findViewById(R.id.edt_password);
        edtConfirm = view.findViewById(R.id.edt_confirm);
        spCity = view.findViewById(R.id.sp_city);
        edtVerification = view.findViewById(R.id.edt_verification);
        btnSend = view.findViewById(R.id.btn_send);
        edtInstagram = view.findViewById(R.id.edt_instagram);
        tvStatus = view.findViewById(R.id.tv_status);
        textInputLayoutInstagram = view.findViewById(R.id.txt_input_layout_instagram);
        textInputLayoutVerification = view.findViewById(R.id.txt_input_layout_verification);
        cardView = view.findViewById(R.id.base_cardview);
        arrow = view.findViewById(R.id.arrow_button);
        hiddenView = view.findViewById(R.id.hidden_view);
        tvFollower = view.findViewById(R.id.tv_followers);
        tvFollowing = view.findViewById(R.id.tv_following);
        tvUsername = view.findViewById(R.id.tv_username);
        tvTotalPost = view.findViewById(R.id.tv_total_post);
        tvTotalComment = view.findViewById(R.id.tv_total_comment);
        tvTotalLike = view.findViewById(R.id.tv_total_like);
        tvMinAge = view.findViewById(R.id.tv_min_age);
        tvMaxAge = view.findViewById(R.id.tv_max_age);
        tvMale = view.findViewById(R.id.tv_male);
        tvFemale = view.findViewById(R.id.tv_female);
        tvTimePosting = view.findViewById(R.id.tv_time_posting);
        tvServiceType = view.findViewById(R.id.tv_service_type);
        tvCategory = view.findViewById(R.id.tv_category);
        menuAdd = view.findViewById(R.id.menu_add);
        btnVerify = view.findViewById(R.id.btn_verify);
    }

    private void bindData(View view) {
        Glide.with(view.getContext())
                .load("https://cdn.myanimelist.net/images/characters/2/373501.jpg")
                .apply(new RequestOptions().override(120, 120))
                .into(imageView);

        try {
            user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
            edtName.setText(user.getName());
            edtUsername.setText(user.getUsername());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            code = user.getCityCode();
            cityName = user.getCityName();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SocialMedia.select(getActivity(), new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    SocialMedia socialMedia = new SocialMedia(jsonObject);
                    tvStatus.setVisibility(View.VISIBLE);
                    if (socialMedia.getStatusVerify() == 0) {
                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pending_24, 0, 0, 0);
                        tvStatus.setText("Pending");
                        tvStatus.setTextColor(getResources().getColor(R.color.yellow));
                    } else {
                        tvFollower.setText(socialMedia.getTotalFollower() + "");
                        tvFollowing.setText(socialMedia.getTotalFollowing() + "");
                        tvUsername.setText(socialMedia.getId());
                        tvTotalPost.setText(socialMedia.getTotalPost() + "");
                        tvTotalComment.setText(socialMedia.getTotalComment() + "");
                        tvTotalLike.setText(socialMedia.getTotalLike() + "");
                        tvMinAge.setText(socialMedia.getMarketAgeMin() + "");
                        tvMaxAge.setText(socialMedia.getMarketAgeMax() + "");
                        tvMale.setText(socialMedia.getMarketMale() + "");
                        tvFemale.setText(socialMedia.getMarketFemale() + "");
                        tvTimePosting.setText(socialMedia.getTimePosting() + "");
                        tvCategory.setText(socialMedia.getCategoryName());

                        if(socialMedia.getServiceBio() == 1){
                            tvServiceType.setText("Bio");
                        }
                        if(socialMedia.getServicePost() == 1){
                            tvServiceType.setText("Post");
                        }
                        if(socialMedia.getServiceStory() == 1){
                            tvServiceType.setText("Story");
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex + "", Toast.LENGTH_SHORT).show();
                    Log.d("RUNNN", ex + "");
                }
            }

            @Override
            public void error() {
            }
        });

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);

        loadCity();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean isValid = true;
        switch (item.getItemId()) {
            case R.id.item_save:
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    edtName.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    edtUsername.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    edtEmail.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    edtPhone.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (!edtConfirm.getText().toString().equals(edtPassword.getText().toString())) {
                    edtConfirm.setError(getResources().getString(R.string.password_doesnt_match));
                    isValid = false;
                }

                if (isValid) {
                    if (user.getUserType() == 1) {
                        code = getCode(spCity.getSelectedItem().toString());
                        User.updateBrand(getActivity(), edtName.getText().toString(), edtPhone.getText().toString(), code, false, new User.Callback() {
                            @Override
                            public void success() {
                                update();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(activity, "Update unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        code = getCode(spCity.getSelectedItem().toString());
                        User.updateMarketer(activity, edtName.getText().toString(), edtPhone.getText().toString(), code, false, new User.Callback() {
                            @Override
                            public void success() {
                                update();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(activity, "Update unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Toast.makeText(activity, "Berhasil", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.item_logout:
                User.logout();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void update(){
        Toast.makeText(activity, "Update successful", Toast.LENGTH_SHORT).show();
        user.setName(edtName.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setCityCode(code);
        Global.setShared(Global.SHARED_INDEX.USER, new Gson().toJson(user));
        HomeActivity homeActivity = (HomeActivity) activity;
        homeActivity.profileFragment();
    }

    public void loadCity() {
        City.select(getActivity(), new City.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                cities.clear();
                int idx = 0;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        City city = new City(data.getJSONObject(i));
                        cities.add(city);
                        if (cities.get(i).getName().equalsIgnoreCase(cityName)) {
                            idx = i;
                        }
//                        Log.d("RUNNNNN", "idx : " + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*Log.d("RUNNNNN", "city : " + cityName);
                Log.d("RUNNNNN", "idxCity : " + idx);*/
                adapter.notifyDataSetChanged();
                spCity.setSelection(idx, true);
            }

            @Override
            public void error() {

            }
        });
    }

    public int getCode(String cityName) {
        int index = -1;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getName().equals(cityName)) {
                index = i;
                break;
            }
        }
        return cities.get(index).getCode();
    }
}