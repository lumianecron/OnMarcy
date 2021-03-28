package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import model.Campaign;

public class ContentActivity extends AppCompatActivity {
    TextView tvTitle, tvDescription, tvPrice, tvBrand, tvInstagram, tvDate, tvTime, tvDuration, tvCategory, tvAge, tvGender, tvLocation;
    private Campaign campaign;
    public static String EXTRA_CAMPAIGN = "campaign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getSupportActionBar().setTitle(getString(R.string.content));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindView();
        if(getIntent().hasExtra(EXTRA_CAMPAIGN)){
            campaign = getIntent().getParcelableExtra(EXTRA_CAMPAIGN);
            bindData();
        }
    }

    private void bindData() {
        tvTitle.setText(campaign.getTitle());
        tvDescription.setText(campaign.getNotes());
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        tvPrice.setText(getResources().getString(R.string.price_format, nf.format(campaign.getPrice())));
        tvBrand.setText(campaign.getBrandName());
        tvDate.setText(campaign.getDate());
        tvTime.setText(campaign.getTime());
        tvDuration.setText(campaign.getDuration() + "");
        tvCategory.setText(campaign.getCategoryName());
        tvAge.setText(campaign.getAgeMin() + " - " + campaign.getAgeMax());
        int gender = campaign.getGender();
        if(gender == 1) tvGender.setText(getString(R.string.male));
        if(gender == 2) tvGender.setText(getString(R.string.female));
        if(gender == 3) tvGender.setText(getString(R.string.all));
        tvLocation.setText(campaign.getCityName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindView() {
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvBrand = findViewById(R.id.tv_brand);
        tvInstagram = findViewById(R.id.tv_instagram);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvDuration = findViewById(R.id.tv_duration);
        tvCategory = findViewById(R.id.tv_category);
        tvAge = findViewById(R.id.tv_age);
        tvGender = findViewById(R.id.tv_gender);
        tvLocation = findViewById(R.id.tv_location);
    }
}