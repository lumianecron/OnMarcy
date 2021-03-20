package com.android.onmarcy;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import model.Campaign;

public class CreateDialogFragment extends DialogFragment {
    public static final String TAG = "create_dialog";
    private TextInputEditText edtTitle, edtNotes, edtMax, edtMin, edtDuration, edtPrice, edtTime;
    private TextInputLayout layoutEdtTime;
    private RadioButton rbMale, rbFemale, rbAll;
    private Activity activity;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;

    public CreateDialogFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static CreateDialogFragment display(FragmentManager fragmentManager){
        CreateDialogFragment createDialogFragment = new CreateDialogFragment(fragmentManager);
        createDialogFragment.show(fragmentManager, TAG);
        return createDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_dialog, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        bindView(getView());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    Fragment fragment = HomeFragment.newInstance();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
            });
            toolbar.setTitle("Create Campaign");
            toolbar.inflateMenu(R.menu.create_menu);
            toolbar.setOnMenuItemClickListener(item -> {
                boolean isValid = true;

                if (TextUtils.isEmpty(edtTitle.getText().toString())) {
                    edtTitle.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMax.getText().toString())) {
                    edtMax.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMin.getText().toString())) {
                    edtMin.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtTime.getText().toString())) {
                    edtTime.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtDuration.getText().toString())) {
                    edtDuration.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtPrice.getText().toString())) {
                    edtPrice.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (isValid) {
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
                            dismiss();
                            Fragment fragment = HomeFragment.newInstance();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        }

                        @Override
                        public void error() {
                            Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("RUNNN", "attempt failed");
                }

                return true;
            });
        }

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        currentTime.set(Calendar.HOUR_OF_DAY, hour);
                        currentTime.set(Calendar.MINUTE, minute);
                        edtTime.setText(mFormat.format(currentTime.getTime()));
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private void bindView(View view){
        edtTitle = view.findViewById(R.id.edt_title);
        edtNotes = view.findViewById(R.id.edt_notes);
        edtMax = view.findViewById(R.id.edt_max_age);
        edtMin = view.findViewById(R.id.edt_min_age);
        edtDuration = view.findViewById(R.id.edt_duration);
        edtPrice = view.findViewById(R.id.edt_price);
        rbMale = view.findViewById(R.id.rb_male);
        rbFemale = view.findViewById(R.id.rb_female);
        rbAll = view.findViewById(R.id.rb_all);
        edtTime = view.findViewById(R.id.edt_time);
        layoutEdtTime = view.findViewById(R.id.layout_edt_time);
    }
}
