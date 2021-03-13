package com.android.onmarcy;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import model.Campaign;
import model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCampaignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCampaignFragment extends Fragment {
    TextInputEditText edtTitle, edtNotes, edtMax, edtMin, edtDuration, edtPrice, edtTime;
    TextInputLayout layoutEdtTime;
    RadioButton rbMale, rbFemale, rbAll;
    Button btnCreate;
    private Activity activity;

    public CreateCampaignFragment() {
        // Required empty public constructor
    }

    public static CreateCampaignFragment newInstance() {
        CreateCampaignFragment fragment = new CreateCampaignFragment();
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
        return inflater.inflate(R.layout.fragment_create_campaign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        bindView(getView());

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        edtTime.setText(hour + ":" + minute);
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString();
                String notes = edtNotes.getText().toString();
                int min = Integer.parseInt(edtMin.getText().toString());
                int max = Integer.parseInt(edtMax.getText().toString());
                int gender = 0;
                if(rbMale.isSelected()) gender = 1;
                if(rbFemale.isSelected()) gender = 2;
                if(rbAll.isSelected()) gender = 3;
                int duration = Integer.parseInt(edtDuration.getText().toString());
                int price = Integer.parseInt(edtPrice.getText().toString());
                Campaign.insert(activity, 2, title, notes, min, max, gender, duration, price, false, new Campaign.Callback() {
                    @Override
                    public void success() {
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error() {
                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void bindView(View view){
        edtTitle = view.findViewById(R.id.edt_title);
        edtNotes = view.findViewById(R.id.edt_notes);
        edtMax = view.findViewById(R.id.edt_max_age);
        edtMin = view.findViewById(R.id.edt_min_age);
        edtDuration = view.findViewById(R.id.edt_duration);
        edtPrice = view.findViewById(R.id.edt_price);
        btnCreate = view.findViewById(R.id.btn_create);
        rbMale = view.findViewById(R.id.rb_male);
        rbFemale = view.findViewById(R.id.rb_female);
        rbAll = view.findViewById(R.id.rb_all);
        edtTime = view.findViewById(R.id.edt_time);
        layoutEdtTime = view.findViewById(R.id.layout_edt_time);
    }
}