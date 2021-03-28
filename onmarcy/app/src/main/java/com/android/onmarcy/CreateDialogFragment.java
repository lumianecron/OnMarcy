package com.android.onmarcy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Campaign;
import model.City;

public class CreateDialogFragment extends DialogFragment {
    public static final String TAG = "create_dialog";
    private TextInputEditText edtTitle, edtNotes, edtMax, edtMin, edtDuration, edtPrice, edtTime, edtDate;
    private TextInputLayout textInputLayoutCategory;
    private AutoCompleteTextView autoCompleteTextView;
    private RadioButton rbMale, rbFemale, rbAll;
    private SearchableSpinner spCity;
    private Activity activity;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private String selectedCategory = "";
    private int categoryCode = 0;
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayAdapter<City> adapter;
    private int cityCode = 0;

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
        registerForContextMenu(autoCompleteTextView);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showContextMenu();
            }
        });

        loadCity();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    Fragment fragment = HomeFragment.newInstance();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
            });
            toolbar.setTitle(R.string.create_campaign);
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
                if(selectedCategory.equals("")){
                    autoCompleteTextView.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtDate.getText().toString())) {
                    edtDate.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (spCity.getSelectedItem() != null) {
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).getName().equals(spCity.getSelectedItem().toString())) {
                            cityCode = cities.get(i).getCode();
                        }
                    }
                }
                
                if(cityCode == 0){
                    isValid = false;
                    Toast.makeText(activity, getString(R.string.please_choose_city), Toast.LENGTH_SHORT).show();
                }

                if (isValid) {
                    String title = edtTitle.getText().toString();
                    String notes = edtNotes.getText().toString();
                    int min = Integer.parseInt(edtMin.getText().toString());
                    int max = Integer.parseInt(edtMax.getText().toString());
                    int gender = 0;
                    if(rbMale.isChecked()) gender = 1;
                    if(rbFemale.isChecked()) gender = 2;
                    if(rbAll.isChecked()) gender = 3;
                    int duration = Integer.parseInt(edtDuration.getText().toString());
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    String[] date = edtDate.getText().toString().split("/");
                    String formattedDate = date[2] + "-" + date[1] + "-" + date[0];
                    String time = edtTime.getText().toString();

                    Campaign.insert(activity, categoryCode, title, notes, min, max, gender, duration, price, formattedDate, time, cityCode, false, new Campaign.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(activity, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                            dismiss();
                            Fragment fragment = HomeFragment.newInstance();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        }

                        @Override
                        public void error() {
                            Toast.makeText(activity, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
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
                timePickerDialog.setTitle(getString(R.string.select_time));
                timePickerDialog.show();
            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        currentDate.set(Calendar.DAY_OF_MONTH, date);
                        currentDate.set(Calendar.MONTH, month);
                        currentDate.set(Calendar.YEAR, year);
                        edtDate.setText(mFormat.format(currentDate.getTime()));
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
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

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        activity.getMenuInflater().inflate(R.menu.context_menu_category, menu);
        menu.setHeaderTitle("Choose Category");

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onContextItemSelected(menuItem);
                return true;
            }
        };

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setOnMenuItemClickListener(listener);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        selectedCategory = item.toString();
        textInputLayoutCategory.setPlaceholderText(selectedCategory);
        if (selectedCategory.equals(getString(R.string.category1))) categoryCode = 1;
        if (selectedCategory.equals(getString(R.string.category2))) categoryCode = 2;
        if (selectedCategory.equals(getString(R.string.category3))) categoryCode = 3;
        if (selectedCategory.equals(getString(R.string.category4))) categoryCode = 4;
        if (selectedCategory.equals(getString(R.string.category5))) categoryCode = 5;
        if (selectedCategory.equals(getString(R.string.category6))) categoryCode = 6;
        if (selectedCategory.equals(getString(R.string.category7))) categoryCode = 7;
        if (selectedCategory.equals(getString(R.string.category8))) categoryCode = 8;
        if (selectedCategory.equals(getString(R.string.category9))) categoryCode = 9;
        return super.onContextItemSelected(item);
    }

    public void loadCity() {
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
        textInputLayoutCategory = view.findViewById(R.id.txt_input_layout_category);
        autoCompleteTextView = view.findViewById(R.id.autoComplete);
        edtDate = view.findViewById(R.id.edt_date);
        spCity = view.findViewById(R.id.sp_city);
    }
}
