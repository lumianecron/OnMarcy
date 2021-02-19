package com.android.onmarcy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvJoin;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = true;
                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    edtUsername.setError(getResources().getString(R.string.please_fill_out_this_field));
                    success = false;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError(getResources().getString(R.string.please_fill_out_this_field));
                    success = false;
                }

                if (success) {
                    /*String username = binding.edtUsername.getText().toString();
                    String password = binding.edtPassword.getText().toString();
                    User.select(getActivity(), username, password, true, new User.CallbackSelect() {
                        @Override
                        public void success(JSONObject data) {
                            User user = new User(data);
//                            Global.setShared(Global.SHARED_INDEX.USER, new Gson().toJson(user));
//                            Intent intent;
//                            if(user.getName().equalsIgnoreCase("") && user.getEmail().equalsIgnoreCase("")){
//                                intent = new Intent(getActivity(),  NewActivity.class);
//                            }
//                            else{
//                                intent = new Intent(getActivity(),  HomeActivity.class);
//                            }
//                            intent.putExtra("userLogin", user);
//                            startActivity(intent);
//                            getActivity().finish();
//                            Global.showLoading(getActivity(), "INFO", "Login successful!");
                            Toast.makeText(getActivity(), "Login successful!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void error() {
//                            Global.showLoading(getActivity(), "INFO", "Login unsuccessful!");
                            Toast.makeText(getActivity(), "Login unsuccessful!", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void bindView(View view) {
        edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);
        btnLogin = view.findViewById(R.id.btn_login);
        tvJoin = view.findViewById(R.id.tv_join);
    }
}