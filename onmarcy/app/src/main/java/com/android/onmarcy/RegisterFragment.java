package com.android.onmarcy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.onmarcy.databinding.FragmentRegisterBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
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
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                if(TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                    binding.edtEmail.setError("Please fill out this field");
                    isValid = false;
                }else{
                    if(binding.edtEmail.getText().toString().contains("@") && binding.edtEmail.getText().toString().contains(".")){
                        binding.edtEmail.setError("Email format is not valid");
                        isValid = false;
                    }
                }
                if(TextUtils.isEmpty(binding.edtUsername.getText().toString())) {
                    binding.edtUsername.setError("Please fill out this field");
                    isValid = false;
                }
                if(TextUtils.isEmpty(binding.edtPassword.getText().toString())) {
                    binding.edtPassword.setError("Please fill out this field");
                    isValid = false;
                }
                if(TextUtils.isEmpty(binding.edtConfirm.getText().toString())) {
                    binding.edtConfirm.setError("Please fill out this field");
                    isValid = false;
                }
                if(TextUtils.isEmpty(binding.edtName.getText().toString())) {
                    binding.edtName.setError("Please fill out this field");
                    isValid = false;
                }
                if(TextUtils.isEmpty(binding.edtPhone.getText().toString())) {
                    binding.edtPhone.setError("Please fill out this field");
                    isValid = false;
                }
                if(TextUtils.isEmpty(binding.edtReferral.getText().toString())) {
                    binding.edtReferral.setError("Please fill out this field");
                    isValid = false;
                }
                if(binding.spCity.getSelectedItem().toString().equals("Select city")){

                }

                if(isValid){
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                }
            }
        });
    }
}