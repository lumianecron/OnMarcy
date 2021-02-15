package com.android.onmarcy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.databinding.FragmentRegisterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import model.City;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    ArrayList<City> cities = new ArrayList<>();

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        City.select(getActivity(), new City.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        City city = new City(data.getJSONObject(i));
                        cities.add(city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error() {

            }
        });
        ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(getContext(), android.R.layout.simple_list_item_1, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCity.setAdapter(cityAdapter);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                    binding.edtEmail.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                } else {
                    if (binding.edtEmail.getText().toString().contains("@") && binding.edtEmail.getText().toString().contains(".")) {
                        binding.edtEmail.setError(getResources().getString(R.string.email_format_is_not_valid));
                        isValid = false;
                    }
                }
                if (TextUtils.isEmpty(binding.edtUsername.getText().toString())) {
                    binding.edtUsername.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(binding.edtPassword.getText().toString())) {
                    binding.edtPassword.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(binding.edtConfirm.getText().toString())) {
                    binding.edtConfirm.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(binding.edtName.getText().toString())) {
                    binding.edtName.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(binding.edtPhone.getText().toString())) {
                    binding.edtPhone.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(binding.edtReferral.getText().toString())) {
                    binding.edtReferral.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (!binding.cbPolicy.isChecked()) {
                    isValid = false;
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_window_agreement, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 300);
                }
                if (!binding.edtConfirm.getText().toString().equals(binding.edtPassword.getText().toString())) {
                    binding.edtConfirm.setError(getResources().getString(R.string.password_doesnt_match));
                    isValid = false;
                }

                int code = 0;
                for (int i = 0; i < cities.size(); i++) {
                    if(cities.get(i).getName().equals(binding.spCity.getSelectedItem().toString())){
                        code = cities.get(i).getCode();
                    }
                }
                if(code == 0) isValid = false;

//                int type = binding.spType.getSelectedItem().equals("Brand") ? 1 : 2;
//                Toast.makeText(getActivity(), type + "", Toast.LENGTH_SHORT).show();

                if (isValid) {
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                }
            }
        });

        binding.tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TermsOfServiceDialog dialog = new TermsOfServiceDialog(getActivity());
                dialog.dialogResult = new TermsOfServiceDialog.OnMyDialogResult() {
                    @Override
                    public void finish(boolean isClose) {

                    }
                };
                dialog.show();
            }
        });
    }
}