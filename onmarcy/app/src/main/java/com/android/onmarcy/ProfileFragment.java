package com.android.onmarcy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    CircleImageView imageView;
    TextInputEditText edtName, edtUsername, edtEmail, edtPhone, edtPassword, edtConfirm, edtVerification, edtInstagram;
    TextInputLayout textInputLayoutInstagram, textInputLayoutVerification;
    TextView tvStatus;
    SearchableSpinner spCity;
    Button btnSend;
    private ArrayList<City> cities = new ArrayList<>();
    private int code = 0;
    private String cityName = "";
    ArrayAdapter<City> adapter;
    private User user;

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
                int code = -1;
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getName().equals(spCity.getSelectedItem().toString())) {
                        code = cities.get(i).getCode();
                        break;
                    }
                }
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
    }

    private void bindData(View view) {
        Glide.with(view.getContext())
                .load("https://i2.wp.com/popculture.id/wp-content/uploads/2020/01/sung-jin-woo-solo-leveling.jpg?fit=770%2C513&ssl=1")
                .apply(new RequestOptions().override(120, 120))
                .into(imageView);
        JSONObject jsonObject = new JSONObject();
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

        if (user.getUserType() == 1) { //brand
            tvStatus.setVisibility(View.INVISIBLE);
            textInputLayoutInstagram.setVisibility(View.INVISIBLE);
            btnSend.setVisibility(View.INVISIBLE);
            textInputLayoutVerification.setVisibility(View.INVISIBLE);
        } else {
            SocialMedia.select(getActivity(), new SocialMedia.CallbackSelect() {
                @Override
                public void success(JSONObject jsonObject) {
                    try {
                        SocialMedia socialMedia = new SocialMedia(jsonObject);
                        if (socialMedia.getStatusVerify() == 0) {
                            tvStatus.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex + "", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void error() {
                }
            });
        }

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
        String errorMessage = "";

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
                    if(user.getUserType() == 1){
                        int code = -1;
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getName().equals(spCity.getSelectedItem().toString())) {
                                code = cities.get(i).getCode();
                                break;
                            }
                        }
                        User.updateBrand(getActivity(), edtName.getText().toString(), edtPhone.getText().toString(), code, true, new User.Callback() {
                            @Override
                            public void success() {
                                Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(getActivity(), "Update unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        int code = -1;
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getName().equals(spCity.getSelectedItem().toString())) {
                                code = cities.get(i).getCode();
                                break;
                            }
                        }
                        User.updateMarketer(getActivity(), edtName.getText().toString(), edtPhone.getText().toString(), code, true, new User.Callback() {
                            @Override
                            public void success() {
                                Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(getActivity(), "Update unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Toast.makeText(getActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
                }
            case R.id.item_logout:
                User.logout();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
        }
        return super.onOptionsItemSelected(item);
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
                        Log.d("RUNNNNN", "idx : " + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("RUNNNNN", "city : " + cityName);
                Log.d("RUNNNNN", "idxCity : " + idx);
                adapter.notifyDataSetChanged();
                spCity.setSelection(idx, true);
            }

            @Override
            public void error() {

            }
        });
    }
}