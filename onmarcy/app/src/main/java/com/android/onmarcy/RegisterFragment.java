package com.android.onmarcy;

import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    TextInputEditText edtEmail, edtUsername, edtPassword, edtConfirm, edtReferral, edtName, edtPhone;
    TextView tvTerms;
    Spinner spCity, spType;
    CheckBox cbPolicy;
    Button btnRegister;
    private ArrayList<City> cities = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

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
        ArrayAdapter<City> cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(cityAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                int code = 0;
                int type = 1;
                String errorMessage = "";

                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    edtEmail.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                } else {
                    if (!edtEmail.getText().toString().contains("@") || !edtEmail.getText().toString().contains(".")) {
                        edtEmail.setError(getResources().getString(R.string.email_format_is_not_valid));
                        isValid = false;
                    }
                }
                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    edtUsername.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtConfirm.getText().toString())) {
                    edtConfirm.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (!edtConfirm.getText().toString().equals(edtPassword.getText().toString())) {
                    edtConfirm.setError(getResources().getString(R.string.password_doesnt_match));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    edtName.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    edtPhone.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (spCity.getSelectedItem() != null) {
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).getName().equals(spCity.getSelectedItem().toString())) {
                            code = cities.get(i).getCode();
                        }
                    }
                }

                if (spType.getSelectedItemPosition() == 1) {
                    type = 2;
                }

                if (!cbPolicy.isChecked() || code == 0) {
                    if (!cbPolicy.isChecked()) {
                        isValid = false;
                        errorMessage = getString(R.string.popup_agreement);
                        /*LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        CustomPopupWindowAgreement.errMessage = getString(R.string.popup_agreement);
                        View popupView = inflater.inflate(R.layout.popup_window_agreement, null);
                        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 300);*/
                    }

                    if (code == 0) {
                        isValid = false;
                        errorMessage = getString(R.string.please_choose_city);
                    }

                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }

                if (isValid) {
                    User.insert(getActivity(), edtUsername.getText().toString(), edtPassword.getText().toString(), edtEmail.getText().toString(), edtName.getText().toString(), edtPhone.getText().toString(), code, type, edtReferral.getText().toString(), true, new User.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                        }

                        @Override
                        public void error() {
                            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Log.d("RUNNN", "attempt failed");
                }
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
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

    private void bindView(View view) {
        edtEmail = view.findViewById(R.id.edt_email);
        edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);
        edtConfirm = view.findViewById(R.id.edt_confirm);
        edtReferral = view.findViewById(R.id.edt_referral);
        edtName = view.findViewById(R.id.edt_name);
        edtPhone = view.findViewById(R.id.edt_phone);
        spCity = view.findViewById(R.id.sp_city);
        spType = view.findViewById(R.id.sp_type);
        cbPolicy = view.findViewById(R.id.cb_policy);
        tvTerms = view.findViewById(R.id.tv_terms);
        btnRegister = view.findViewById(R.id.btn_register);
    }
}