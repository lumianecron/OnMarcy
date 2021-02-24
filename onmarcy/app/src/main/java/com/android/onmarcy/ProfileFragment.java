package com.android.onmarcy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.City;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    CircleImageView imageView;
    TextView tvFollowers, tvFollowing;
    EditText edtName, edtUsername, edtEmail, edtPhone, edtPassword, edtConfirm;
    SearchableSpinner spCity;
    private ArrayList<City> cities = new ArrayList<>();
    private int code = 0;
    private String cityName = "";
    ArrayAdapter<City> adapter;

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
    }

    private void bindView(View view) {
        imageView = view.findViewById(R.id.image_view);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        edtName = view.findViewById(R.id.edt_name);
        edtUsername = view.findViewById(R.id.edt_username);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtPassword = view.findViewById(R.id.edt_password);
        edtConfirm = view.findViewById(R.id.edt_confirm);
        spCity = view.findViewById(R.id.sp_city);
    }

    private void bindData(View view) {
        Glide.with(view.getContext())
                .load("https://i2.wp.com/popculture.id/wp-content/uploads/2020/01/sung-jin-woo-solo-leveling.jpg?fit=770%2C513&ssl=1")
                .apply(new RequestOptions().override(120, 120))
                .into(imageView);
        tvFollowers.setText("12");
        tvFollowing.setText("5");
        edtName.setText("Enjirou");
        edtUsername.setText("enjirou123");
        edtEmail.setText("shiruku0004@gmail.com");
        edtPhone.setText("087855161565");

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

        if (item.getItemId() == R.id.item_save) {
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
                Toast.makeText(getActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadCity(){
        City.select(getActivity(), new City.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                cities.clear();
                int idx = 0;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        City city = new City(data.getJSONObject(i));
                        cities.add(city);
                        if(cities.get(i).getCode() == 205){
                            idx = i;
                        }
                        Log.d("RUNNNNN", "idx : " + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                spCity.setSelection(idx, true);
            }

            @Override
            public void error() {

            }
        });
    }
}