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
import android.widget.Toast;

import com.android.onmarcy.databinding.FragmentLoginBinding;
import com.google.gson.Gson;

import org.json.JSONObject;

import model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;

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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            boolean isValid = true;
            @Override
            public void onClick(View view) {
                /*boolean success = true;
                if(TextUtils.isEmpty(binding.edtUsername.getText().toString())){
                    isValid = false;
                    binding.edtUsername.setError(getResources().getString(R.string.please_fill_out_this_field));
                    success = false;
                }
                if(TextUtils.isEmpty(binding.edtPassword.getText().toString())){
                    isValid = false;
                    binding.edtPassword.setError(getResources().getString(R.string.please_fill_out_this_field));
                    success = false;
                }

                if(success){
                    String username = binding.edtUsername.getText().toString();
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
                    });
                }*/
                Navigation.findNavController(view).navigate(R.id.action_login_to_home);
            }
        });
    }
}