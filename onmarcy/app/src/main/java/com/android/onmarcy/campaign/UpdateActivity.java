package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.onmarcy.MainActivity;
import com.android.onmarcy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import model.Campaign;
import model.City;
import model.SocialMedia;

public class UpdateActivity extends AppCompatActivity {
    public static final String EXTRA_CAMPAIGN = "campaign";
    private TextInputEditText edtTitle, edtNotes, edtMax, edtMin, edtDuration, edtPrice, edtTime, edtDate;
    private AutoCompleteTextView autoCompleteTextView;
    private RadioButton rbMale, rbFemale, rbAll;
    private SearchableSpinner spCity;
    private String selectedCategory = "";
    private int categoryCode = 0;
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayAdapter<City> adapter;
    private int cityCode = 0;
    private Campaign campaign;
    private String username_ig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if (getIntent().hasExtra(EXTRA_CAMPAIGN))
            campaign = getIntent().getParcelableExtra(EXTRA_CAMPAIGN);
        bindView();
        bindData();

        registerForContextMenu(autoCompleteTextView);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showContextMenu();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.update_campaign);

        adapter = new ArrayAdapter<>(UpdateActivity.this, android.R.layout.simple_list_item_1, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);
        loadCity();

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
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
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_category, menu);
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
        autoCompleteTextView.setText(selectedCategory);
        setCategoryCode(selectedCategory);
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
            case R.id.item_update:
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

                if (cityCode == 0) {
                    isValid = false;
                    Toast.makeText(this, getString(R.string.please_choose_city), Toast.LENGTH_SHORT).show();
                }

                if (isValid) {
                    String title = edtTitle.getText().toString();
                    String notes = edtNotes.getText().toString();
                    int min = Integer.parseInt(edtMin.getText().toString());
                    int max = Integer.parseInt(edtMax.getText().toString());
                    int gender = 0;
                    if (rbMale.isChecked()) gender = 1;
                    if (rbFemale.isChecked()) gender = 2;
                    if (rbAll.isChecked()) gender = 3;
                    int duration = Integer.parseInt(edtDuration.getText().toString());
                    String[] date = edtDate.getText().toString().split("/");
                    String formattedDate = date[2] + "-" + date[1] + "-" + date[0];
                    String time = edtTime.getText().toString();

                    Campaign.update(this, campaign.getCodeString(), username_ig, categoryCode, cityCode, title, notes, min, max, gender, formattedDate, time, duration, false, new Campaign.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(UpdateActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void error() {
                            Toast.makeText(UpdateActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("RUNNN", "attempt failed");
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindData() {
        edtTitle.setText(campaign.getTitle());
        edtNotes.setText(campaign.getNotes());
        autoCompleteTextView.setText(campaign.getCategoryName());
        edtMax.setText(String.valueOf(campaign.getAgeMax()));
        edtMin.setText(String.valueOf(campaign.getAgeMin()));
        edtDuration.setText(String.valueOf(campaign.getDuration()));
        edtPrice.setText(String.valueOf(campaign.getPrice()));
        edtTime.setText(campaign.getTime());
        String[] date = campaign.getDate().split("-");
        String formattedDate = date[2] + "/" + date[1] + "/" + date[0];
        edtDate.setText(formattedDate);
        if (campaign.getGender() == 1) rbMale.setChecked(true);
        if (campaign.getGender() == 2) rbFemale.setChecked(true);
        if (campaign.getGender() == 3) rbAll.setChecked(true);
        edtTime.setText(campaign.getTime());

        loadCity();
        setCategoryCode(campaign.getCategoryName());

        SocialMedia.select(this, new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    SocialMedia socialMedia = new SocialMedia(jsonObject);
                    username_ig = socialMedia.getId();
                } catch (Exception ex) {
                    Toast.makeText(UpdateActivity.this, ex + "", Toast.LENGTH_SHORT).show();
                    Log.d("RUNNN", ex + "");
                }
            }

            @Override
            public void error() {
            }
        });
    }

    public void loadCity() {
        City.select(this, new City.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                cities.clear();
                int index = 0;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        City city = new City(data.getJSONObject(i));
                        cities.add(city);
                        if (cities.get(i).getCode() == campaign.getCityCode()) {
                            index = i;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                spCity.setSelection(index, true);
            }

            @Override
            public void error() {

            }
        });
    }

    private void setCategoryCode(String selectedCategory) {
        if (selectedCategory.equals(getString(R.string.category1))) categoryCode = 1;
        if (selectedCategory.equals(getString(R.string.category2))) categoryCode = 2;
        if (selectedCategory.equals(getString(R.string.category3))) categoryCode = 3;
        if (selectedCategory.equals(getString(R.string.category4))) categoryCode = 4;
        if (selectedCategory.equals(getString(R.string.category5))) categoryCode = 5;
        if (selectedCategory.equals(getString(R.string.category6))) categoryCode = 6;
        if (selectedCategory.equals(getString(R.string.category7))) categoryCode = 7;
        if (selectedCategory.equals(getString(R.string.category8))) categoryCode = 8;
        if (selectedCategory.equals(getString(R.string.category9))) categoryCode = 9;
    }

    private void bindView() {
        edtTitle = findViewById(R.id.edt_title);
        edtNotes = findViewById(R.id.edt_notes);
        edtMax = findViewById(R.id.edt_max_age);
        edtMin = findViewById(R.id.edt_min_age);
        edtDuration = findViewById(R.id.edt_duration);
        edtPrice = findViewById(R.id.edt_price);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        rbAll = findViewById(R.id.rb_all);
        edtTime = findViewById(R.id.edt_time);
        autoCompleteTextView = findViewById(R.id.autoComplete);
        edtDate = findViewById(R.id.edt_date);
        spCity = findViewById(R.id.sp_city);
    }
}